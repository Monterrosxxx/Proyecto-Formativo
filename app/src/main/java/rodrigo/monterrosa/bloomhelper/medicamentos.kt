package rodrigo.monterrosa.bloomhelper

import Modelo.claseConexion
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class medicamentos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_medicamentos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtnombreMedicamento = findViewById<EditText>(R.id.txtnombreMedicamento)
        val txtaplicacionMedicamento = findViewById<EditText>(R.id.txtaplicacionMedicamento)
        val btnagregarMedicamento = findViewById<Button>(R.id.btnagregarMedicamento)
        val imgRegresar = findViewById<ImageView>(R.id.imgRegresardemedicamentos)

        btnagregarMedicamento.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {

                val objConexion = claseConexion().cadenaConexion()

                val insertarMedicamentos = objConexion?.prepareStatement("INSERT INTO medicamento (UUID_medicamento, nombre, aplicacionMedicamento) VALUES (?, ?, ?)")!!
                insertarMedicamentos.setString(1,UUID.randomUUID().toString())
                insertarMedicamentos.setString(2, txtnombreMedicamento.text.toString())
                insertarMedicamentos.setString(3, txtaplicacionMedicamento.text.toString())
                insertarMedicamentos.executeUpdate()

                CoroutineScope(Dispatchers.Main).launch {

                    Toast.makeText(this@medicamentos, "Medicamento agregado", Toast.LENGTH_SHORT).show()
                    txtnombreMedicamento.setText("")
                    txtaplicacionMedicamento.setText("")
                }

            }

        }

        imgRegresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}