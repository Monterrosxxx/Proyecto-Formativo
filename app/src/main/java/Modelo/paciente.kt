package Modelo

data class paciente(
    val uuidPaciente: String,
    var nombre: String,
    val apellidos: String,
    val edad: Int,
    val enfermedad: String,
    val numero_habitacion: Int,
    val numero_cama: Int
)
