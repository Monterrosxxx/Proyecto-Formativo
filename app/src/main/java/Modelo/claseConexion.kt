package Modelo

import java.sql.Connection
import java.sql.DriverManager

class claseConexion {

    fun cadenaConexion(): Connection?{
        try {
            val ip = "jdbc:oracle:thin:@192.168.0.8:1521:xe"
            val usuario = "BLOOMHELPER_DEVELOPER"
            val contrasena = "proyectoFormativo"

            val conexion = DriverManager.getConnection(ip, usuario, contrasena)
            return conexion
        }catch (e: Exception){
            println("Este es el error: $e")
            return null
        }
    }

}