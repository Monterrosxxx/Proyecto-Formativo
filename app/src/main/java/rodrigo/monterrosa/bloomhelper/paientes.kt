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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.SQLException
import java.util.UUID

class paientes : AppCompatActivity() {
    private lateinit var rcvPacientes: RecyclerView
    private lateinit var txtnombrePaciente: EditText
    private lateinit var txtapellidoPaciente: EditText
    private lateinit var txtEdad: EditText
    private lateinit var txtEnfermedad: EditText
    private lateinit var txtnumeroHabitacion: EditText
    private lateinit var txtnumeroCama: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_paientes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        setupRecyclerView()
        loadPacientes()
        setupListeners()
    }

    private fun initializeViews() {
        txtnombrePaciente = findViewById(R.id.txtnombrePaciente)
        txtapellidoPaciente = findViewById(R.id.txtapellidoPaciente)
        txtEdad = findViewById(R.id.txtEdad)
        txtEnfermedad = findViewById(R.id.txtEnfermedad)
        txtnumeroHabitacion = findViewById(R.id.txtnumeroHabitacion)
        txtnumeroCama = findViewById(R.id.txtnumeroCama)
        rcvPacientes = findViewById(R.id.rcvPacientes)
    }

    private fun setupRecyclerView() {
        rcvPacientes.layoutManager = LinearLayoutManager(this)
    }

    private fun loadPacientes() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val pacientesDB = obtenerPacientes()
                withContext(Dispatchers.Main) {
                    val adaptador = Adaptador(pacientesDB)
                    rcvPacientes.adapter = adaptador
                }
            } catch (e: SQLException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@paientes, "Error al cargar pacientes: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupListeners() {
        findViewById<Button>(R.id.btnAgregar).setOnClickListener { agregarPaciente() }
        findViewById<ImageView>(R.id.imgRegresar).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun obtenerPacientes(): List<paciente> {
        val listaPacientes = mutableListOf<paciente>()
        var connection: Connection? = null
        try {
            connection = claseConexion().cadenaConexion()
            connection?.createStatement()?.use { statement ->
                statement.executeQuery("SELECT * FROM paciente").use { resultSet ->
                    while (resultSet.next()) {
                        listaPacientes.add(
                            paciente(
                                resultSet.getString("UUID_paciente"),
                                resultSet.getString("nombre"),
                                resultSet.getString("apellidos"),
                                resultSet.getInt("edad"),
                                resultSet.getString("enfermedad"),
                                resultSet.getInt("numero_habitacion"),
                                resultSet.getInt("numero_cama")
                            )
                        )
                    }
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            throw e
        } finally {
            connection?.close()
        }
        return listaPacientes
    }

    private fun agregarPaciente() {
        if (!validarCampos()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                insertarPaciente()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@paientes, "Paciente agregado", Toast.LENGTH_SHORT).show()
                    limpiarCampos()
                    loadPacientes()
                }
            } catch (e: SQLException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@paientes, "Error al agregar paciente: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun validarCampos(): Boolean {
        return txtnombrePaciente.text.isNotBlank() &&
                txtapellidoPaciente.text.isNotBlank() &&
                txtEdad.text.isNotBlank() &&
                txtEnfermedad.text.isNotBlank() &&
                txtnumeroHabitacion.text.isNotBlank() &&
                txtnumeroCama.text.isNotBlank()
    }

    private fun insertarPaciente() {
        var connection: Connection? = null
        try {
            connection = claseConexion().cadenaConexion()
            connection?.prepareStatement(
                "INSERT INTO paciente (UUID_paciente, nombre, apellidos, edad, enfermedad, numero_habitacion, numero_cama) VALUES (?, ?, ?, ?, ?, ?, ?)"
            )?.use { statement ->
                statement.setString(1, UUID.randomUUID().toString())
                statement.setString(2, txtnombrePaciente.text.toString())
                statement.setString(3, txtapellidoPaciente.text.toString())
                statement.setInt(4, txtEdad.text.toString().toInt())
                statement.setString(5, txtEnfermedad.text.toString())
                statement.setInt(6, txtnumeroHabitacion.text.toString().toInt())
                statement.setInt(7, txtnumeroCama.text.toString().toInt())
                statement.executeUpdate()
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            throw e
        } finally {
            connection?.close()
        }
    }

    private fun limpiarCampos() {
        txtnombrePaciente.text.clear()
        txtapellidoPaciente.text.clear()
        txtEdad.text.clear()
        txtEnfermedad.text.clear()
        txtnumeroHabitacion.text.clear()
        txtnumeroCama.text.clear()
    }
}