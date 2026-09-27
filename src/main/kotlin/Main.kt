package org.example

import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val sistema = SistemaPetCare(10)

    println("=== PetCare: Sistema de Gestion de Boxes Veterinarios ===")
    println("")

    sistema.registrarEntrada("CA12CD", "Max", "Golden Retriever", TipoDueno.CONVENIO, "canino")
    sistema.registrarEntrada("CA99ZA", "Luna", "Labrador", TipoDueno.PARTICULAR, "canino")
    sistema.registrarEntrada("FE22TO", "Misi", "Siames", TipoDueno.PARTICULAR, "felino")
    sistema.registrarEntrada("EX44RG", "Loro", "Amazonico", TipoDueno.MUNICIPAL, "exotico", true)
    sistema.registrarEntrada("EX77RG", "Iguana", "Verde", TipoDueno.PARTICULAR, "exotico", false)

    sistema.registrarEntrada("123ABC", "Firulais", "Mestizo", TipoDueno.PARTICULAR, "canino")

    sistema.registrarSalida("CA12CD")
    sistema.registrarSalida("CA99ZA")
    sistema.registrarSalida("FE22TO")
    sistema.registrarSalida("EX44RG")
    sistema.registrarSalida("EX77RG")

    sistema.registrarSalida("ZZ00ZZ")

    println("")
    println("--- Consultas de negocio ---")
    println("Boxes disponibles: " + sistema.boxesDisponibles())
    println("Ingreso promedio: $" + sistema.ingresoPromedioPorPaciente())
    println("Codigos finalizados: " + sistema.codigosPacientesFinalizados())

    val convenio = sistema.pacientesConvenioEnHistorial()
    print("Pacientes convenio: ")
    for (ticket in convenio) {
        print(ticket.paciente.codigoAtencion + " ")
    }
    println("")

    val masTiempo = sistema.pacienteConMasTiempo()
    if (masTiempo != null) {
        println("Paciente con mas tiempo: " + masTiempo.paciente.codigoAtencion)
    }

    sistema.generarReporteCierre()
}