package rodrigo.monterrosa.bloomhelper

import Modelo.claseConexion
import Modelo.paciente
import RecyclerViewHelpers.Adaptador
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class paientes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_paientes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtnombrePaciente = findViewById<EditText>(R.id.txtnombrePaciente)
        val txtapellidoPaciente = findViewById<EditText>(R.id.txtapellidoPaciente)
        val txtEdad = findViewById<EditText>(R.id.txtEdad)
        val txtEnfermedad = findViewById<EditText>(R.id.txtEnfermedad)
        val txtnumeroHabitacion = findViewById<EditText>(R.id.txtnumeroHabitacion)
        val txtnumeroCama = findViewById<EditText>(R.id.txtnumeroCama)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        val imgRegresar = findViewById<ImageView>(R.id.imgRegresar)
        val rcvPacientes = findViewById<RecyclerView>(R.id.rcvPacientes)

        rcvPacientes.layoutManager = LinearLayoutManager(this)

        fun obtenerPacientes():List<paciente>{
            val objConexion = claseConexion().cadenaConexion()
            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM paciente")!!

            val listaPacientes = mutableListOf<paciente>()
            while (resultSet.next() == true) {

                val uuidPaciente = resultSet.getString("UUID_paciente")
                val nombre = resultSet.getString("nombre")
                val apellidos = resultSet.getString("apellidos")
                val edad = resultSet.getInt("edad")
                val enfermedad = resultSet.getString("enfermedad")
                val numero_habitacion = resultSet.getInt("numero_habitacion")
                val numero_cama = resultSet.getInt("numero_cama")

                val valoresJuntos = paciente(uuidPaciente, nombre, apellidos, edad, enfermedad, numero_habitacion, numero_cama)
                listaPacientes.add(valoresJuntos)
            }

            return listaPacientes

        }

        CoroutineScope(Dispatchers.IO).launch {
            val pacientesDB = obtenerPacientes()
            withContext(Dispatchers.Main) {
                val adaptador = Adaptador(pacientesDB)
                rcvPacientes.adapter = adaptador
            }
        }

        btnAgregar.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {

                val objConexion = claseConexion().cadenaConexion()

                val insertarPacientes =
                    objConexion?.prepareStatement("INSERT INTO paciente (UUID_paciente, nombre, apellidos, edad, enfermedad, numero_habitacion, numero_cama) VALUES (?, ?, ?, ?, ?, ?, ?)")!!

                insertarPacientes.setString(1,UUID.randomUUID().toString())
                insertarPacientes.setString(2, txtnombrePaciente.text.toString())
                insertarPacientes.setString(3, txtapellidoPaciente.text.toString())
                insertarPacientes.setInt(4, txtEdad.text.toString().toInt())
                insertarPacientes.setString(5, txtEnfermedad.text.toString())
                insertarPacientes.setInt(6, txtnumeroHabitacion.text.toString().toInt())
                insertarPacientes.setInt(7, txtnumeroCama.text.toString().toInt())
                insertarPacientes.executeUpdate()

                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(this@paientes, "Paciente agregado", Toast.LENGTH_SHORT).show()
                    txtnombrePaciente.setText("")
                    txtapellidoPaciente.setText("")
                    txtEdad.setText("")
                    txtEnfermedad.setText("")
                    txtnumeroHabitacion.setText("")
                    txtnumeroCama.setText("")
                }

            }

        }

        imgRegresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}