package org.example

// comprobante que se genera cuando un paciente termina su atencion
data class Ticket(
    val numeroTicket: Int,
    val paciente: Paciente,
    val minutosAtendido: Long,
    val montoPagado: Double
)
