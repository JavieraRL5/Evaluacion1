package org.example

import java.time.LocalDateTime

abstract class Paciente(
    val codigoAtencion: String,
    val nombre: String,
    val especie: String,
    val fechaIngreso: LocalDateTime,
    val tipoDueno: TipoDueno
) {
    abstract val tarifaBaseHora: Double

    abstract fun calcularCostoBase(minutos: Long): Double
}

class Canino(
    codigoAtencion: String,
    nombre: String,
    especie: String,
    fechaIngreso: LocalDateTime,
    tipoDueno: TipoDueno
) : Paciente(codigoAtencion, nombre, especie, fechaIngreso, tipoDueno) {

    override val tarifaBaseHora: Double = 12000.0

    override fun calcularCostoBase(minutos: Long): Double {
        val horas = minutos / 60.0
        var costo = tarifaBaseHora * horas
        // si el dueño tiene convenio se descuenta el 20% del costo
        if (tipoDueno == TipoDueno.CONVENIO) {
            costo = costo * 0.8
        }
        return costo
    }
}

class Felino(
    codigoAtencion: String,
    nombre: String,
    especie: String,
    fechaIngreso: LocalDateTime,
    tipoDueno: TipoDueno
) : Paciente(codigoAtencion, nombre, especie, fechaIngreso, tipoDueno) {

    override val tarifaBaseHora: Double = 9000.0

    override fun calcularCostoBase(minutos: Long): Double {
        // si la atencion dura menos de 20 minutos no se cobra nada
        if (minutos < 20) {
            return 0.0
        }
        val horas = minutos / 60.0
        return tarifaBaseHora * horas
    }
}

class Exotico(
    codigoAtencion: String,
    nombre: String,
    especie: String,
    fechaIngreso: LocalDateTime,
    tipoDueno: TipoDueno,
    val esSilvestre: Boolean
) : Paciente(codigoAtencion, nombre, especie, fechaIngreso, tipoDueno) {

    override val tarifaBaseHora: Double = 20000.0

    override fun calcularCostoBase(minutos: Long): Double {
        val horas = minutos / 60.0
        var costo = tarifaBaseHora * horas
        // si el animal es silvestre se agrega un recargo del 30%
        if (esSilvestre) {
            costo = costo * 1.3
        }
        return costo
    }
}