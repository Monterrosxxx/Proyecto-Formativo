package rodrigo.monterrosa.bloomhelper

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imgMedicamentos = findViewById<ImageView>(R.id.imgMedicamentos)
        val imgPacientes = findViewById<ImageView>(R.id.imgPacientes)

        imgMedicamentos.setOnClickListener {
            val intent = Intent(this, medicamentos::class.java)
            startActivity(intent)
        }

        imgPacientes.setOnClickListener {
            val intent = Intent(this, paientes::class.java)
            startActivity(intent)
        }

    }
}