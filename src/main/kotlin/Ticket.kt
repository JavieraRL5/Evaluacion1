package org.example

/**
 * Comprobante emitido al finalizar la atención de un paciente.
 */
data class Ticket(
    val numeroTicket: Int,
    val paciente: Paciente,
    val minutosAtendido: Long,
    val montoPagado: Double
)