package org.example

import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDateTime

class SistemaPetCare(val capacidadBoxes: Int = 10) {

    val boxes = mutableListOf<Box>()
    val historialTickets = mutableListOf<Ticket>()
    var recaudacionTotal = 0.0
    val recaudacionPorTipo = mutableMapOf<String, Double>()
    var contadorTickets = 0

    init {
        for (i in 1..capacidadBoxes) {
            boxes.add(Box(i))
        }
    }

    fun esCodigoValido(codigo: String): Boolean {
        if (codigo.length != 6) {
            return false
        }
        val letra1 = codigo[0]
        val letra2 = codigo[1]
        val digito1 = codigo[2]
        val digito2 = codigo[3]
        val letra3 = codigo[4]
        val letra4 = codigo[5]

        if (letra1.isLetter() && letra2.isLetter() && digito1.isDigit() && digito2.isDigit() && letra3.isLetter() && letra4.isLetter()) {
            return true
        }
        return false
    }

    suspend fun registrarEntrada(
        codigoAtencion: String,
        nombre: String,
        especie: String,
        tipoDueno: TipoDueno,
        tipoPaciente: String,
        esSilvestre: Boolean = false
    ) {
        try {
            if (!esCodigoValido(codigoAtencion)) {
                println("Error: el codigo '$codigoAtencion' no tiene el formato correcto. No se registro el paciente.")
                return
            }

            var boxLibre: Box? = null
            for (box in boxes) {
                if (box.estado == EstadoDelBox.LIBRE) {
                    boxLibre = box
                    break
                }
            }

            if (boxLibre == null) {
                println("Error: no hay boxes libres en este momento. No se pudo registrar la entrada.")
                return
            }

            boxLibre.estado = EstadoDelBox.EN_PROCESO
            boxLibre.motivoProceso = "Registrando entrada"
            println("Box " + boxLibre.numero + ": registrando entrada de " + codigoAtencion)

            delay(3000)

            var paciente: Paciente? = null
            val tipoMinuscula = tipoPaciente.lowercase()

            if (tipoMinuscula == "canino") {
                paciente = Canino(codigoAtencion, nombre, especie, LocalDateTime.now(), tipoDueno)
            } else if (tipoMinuscula == "felino") {
                paciente = Felino(codigoAtencion, nombre, especie, LocalDateTime.now(), tipoDueno)
            } else if (tipoMinuscula == "exotico") {
                paciente = Exotico(codigoAtencion, nombre, especie, LocalDateTime.now(), tipoDueno, esSilvestre)
            }

            if (paciente == null) {
                println("Error: el tipo de paciente '$tipoPaciente' no existe. No se registro el paciente.")
                boxLibre.estado = EstadoDelBox.LIBRE
                return
            }

            boxLibre.estado = EstadoDelBox.EN_ATENCION
            boxLibre.pacienteAsignado = paciente
            println("Entrada confirmada: " + codigoAtencion + " fue asignado al box " + boxLibre.numero)

        } catch (e: Exception) {
            println("Ocurrio un error al registrar la entrada. El sistema sigue funcionando.")
        }
    }

    suspend fun registrarSalida(codigoAtencion: String) {
        try {
            var boxEncontrado: Box? = null
            for (box in boxes) {
                if (box.estado == EstadoDelBox.EN_ATENCION) {
                    val paciente = box.pacienteAsignado
                    if (paciente != null && paciente.codigoAtencion == codigoAtencion) {
                        boxEncontrado = box
                        break
                    }
                }
            }

            if (boxEncontrado == null) {
                println("Error: no se encontro un paciente con el codigo '$codigoAtencion'. No se realizo la salida.")
                return
            }

            val paciente = boxEncontrado.pacienteAsignado!!
            boxEncontrado.estado = EstadoDelBox.EN_PROCESO
            boxEncontrado.motivoProceso = "Calculando tarifa"
            println("Box " + boxEncontrado.numero + ": calculando tarifa de " + codigoAtencion)

            delay(6500)

            val minutos = Duration.between(paciente.fechaIngreso, LocalDateTime.now()).toMinutes()
            val monto = calcularMontoFinal(paciente, minutos)

            if (monto < 0) {
                println("Error: el monto calculado no es valido para $codigoAtencion.")
                boxEncontrado.estado = EstadoDelBox.EN_ATENCION
                return
            }

            contadorTickets = contadorTickets + 1
            val ticket = Ticket(contadorTickets, paciente, minutos, monto)
            historialTickets.add(ticket)
            recaudacionTotal = recaudacionTotal + monto

            val tipoNombre = paciente.javaClass.simpleName
            var totalAnterior = recaudacionPorTipo[tipoNombre]
            if (totalAnterior == null) {
                totalAnterior = 0.0
            }
            recaudacionPorTipo[tipoNombre] = totalAnterior + monto

            boxEncontrado.estado = EstadoDelBox.LIBRE
            boxEncontrado.pacienteAsignado = null
            println("Salida confirmada. Ticket numero " + ticket.numeroTicket + " - " + codigoAtencion + " - $" + monto)

        } catch (e: Exception) {
            println("Ocurrio un error al registrar la salida. El sistema sigue funcionando.")
        }
    }

    fun calcularMontoFinal(paciente: Paciente, minutos: Long): Double {
        val costoBase = paciente.calcularCostoBase(minutos)
        if (costoBase < 0) {
            return -1.0
        }
        var montoConIva = costoBase * 1.19
        if (paciente.tipoDueno == TipoDueno.MUNICIPAL) {
            montoConIva = montoConIva * 0.5
        }
        return montoConIva
    }

    fun boxesDisponibles(): Int {
        var contador = 0
        for (box in boxes) {
            if (box.estado == EstadoDelBox.LIBRE) {
                contador = contador + 1
            }
        }
        return contador
    }

    fun pacientesConvenioEnHistorial(): List<Ticket> {
        val lista = mutableListOf<Ticket>()
        for (ticket in historialTickets) {
            if (ticket.paciente.tipoDueno == TipoDueno.CONVENIO) {
                lista.add(ticket)
            }
        }
        return lista
    }

    fun ingresoPromedioPorPaciente(): Double {
        if (historialTickets.isEmpty()) {
            return 0.0
        }
        return recaudacionTotal / historialTickets.size
    }

    fun codigosPacientesFinalizados(): List<String> {
        val lista = mutableListOf<String>()
        for (ticket in historialTickets) {
            lista.add(ticket.paciente.codigoAtencion)
        }
        return lista
    }

    fun pacienteConMasTiempo(): Ticket? {
        if (historialTickets.isEmpty()) {
            return null
        }
        var mayor = historialTickets[0]
        for (ticket in historialTickets) {
            if (ticket.minutosAtendido > mayor.minutosAtendido) {
                mayor = ticket
            }
        }
        return mayor
    }

    fun tipoConMasIngresos(): String? {
        if (recaudacionPorTipo.isEmpty()) {
            return null
        }
        var mejorTipo = ""
        var mejorMonto = -1.0
        for (entrada in recaudacionPorTipo) {
            if (entrada.value > mejorMonto) {
                mejorMonto = entrada.value
                mejorTipo = entrada.key
            }
        }
        return mejorTipo
    }

    fun generarReporteCierre() {
        println("")
        println("===== REPORTE DE CIERRE DE TURNO - PetCare =====")
        for (ticket in historialTickets) {
            println("Ticket " + ticket.numeroTicket + " | " + ticket.paciente.javaClass.simpleName + " | " + ticket.paciente.codigoAtencion + " | " + ticket.minutosAtendido + " min | $" + ticket.montoPagado)
        }
        println("-------------------------------------------------")
        println("Total recaudado: $" + recaudacionTotal)
        println("Pacientes atendidos: " + historialTickets.size)
        println("Ingreso promedio: $" + ingresoPromedioPorPaciente())
        val mejorTipo = tipoConMasIngresos()
        if (mejorTipo == null) {
            println("Tipo con mas ingresos: N/A")
        } else {
            println("Tipo con mas ingresos: " + mejorTipo)
        }
        println("Boxes disponibles al cierre: " + boxesDisponibles())
        println("==================================================")
    }
}