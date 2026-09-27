package org.example

class Box(val numero: Int) {
    var estado: EstadoDelBox = EstadoDelBox.LIBRE
    var pacienteAsignado: Paciente? = null
    var motivoProceso: String = ""
}