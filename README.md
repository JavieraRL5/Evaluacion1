PetCare

Sistema de Gestión de Boxs de una Veterinaria

Descripción:
Sistema en Kotlin que administra el ingreso y salida de pacientes en los box de atención de una veterinaria, calcula tarifas y genera un reporte de cierre de turno.

¿Cómo se ejecuta el proyecto?

Abrir el proyecto en IntelliJ IDEA.
Esperar a que Gradle sincronice las dependencias (incluye kotlinx-coroutines-core).
Abrir el archivo src --> main-->kotlin-->Main.kt
Ejecutar la función main y luego (boton de Run).
El programa se demora aproximadamente 1 minuto en terminar, ya que simula tiempos de espera con corrutinas

Estructura del proyecto
TipoDueno.kt: enum(enumeración) con los tipos de dueno (Particular, Convenio, Municipal).
Paciente.kt: clase abstracta Paciente y sus subclases Canino, Felino y Exótico.
EstadoDelBox.kt: enum(enumeración) con los estados posibles de un box.
Box.kt: clase que representa un box de atención.
Ticket.kt: clase que representa el comprobante de una atención ya  finalizada.
SistemaPetCare.kt: lógica principal del sistema (registro de entrada y salida, calculo de tarifas, consultas y reporte).
Main.kt: punto de entrada del programa, con datos de prueba.
