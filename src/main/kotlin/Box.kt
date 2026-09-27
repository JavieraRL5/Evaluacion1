package org.example

// representa un box de atencion. guarda el estado actual,
// el paciente que tiene asignado (si es que tiene) y el motivo si esta en proceso
class Box(val numero: Int) {
    var estado: EstadoDelBox = EstadoDelBox.LIBRE
    var pacienteAsignado: Paciente? = null
    var motivoProceso: String = ""
}
