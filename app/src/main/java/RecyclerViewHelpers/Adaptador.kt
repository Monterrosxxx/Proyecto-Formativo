package RecyclerViewHelpers

import Modelo.claseConexion
import Modelo.paciente
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rodrigo.monterrosa.bloomhelper.R
import java.util.UUID

class Adaptador (var Datos: List<paciente>): RecyclerView.Adapter<ViewHolde>() {

    fun actualizarLista(nuevosLista: List<paciente>) {
        Datos = nuevosLista
        notifyDataSetChanged()
    }

    fun actualizarListaDespuesdeAztualizarlosDatos(uuid: String, nuvoNombre: String) {
        val index = Datos.indexOfFirst { it.uuidPaciente == uuid }
        Datos[index].nombre = nuvoNombre
        notifyItemChanged(index)
    }

    fun eliminarPaciente(nombre: String, postion: Int) {

        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(postion)

        GlobalScope.launch(Dispatchers.IO) {
            val objConexion = claseConexion().cadenaConexion()
            val eliminarPaciente = objConexion?.prepareStatement("DELETE FROM paciente WHERE nombre = ?")!!
            eliminarPaciente.setString(1, nombre)
            eliminarPaciente.executeUpdate()

            val commit = objConexion.prepareStatement("COMMIT")
            commit.executeUpdate()
        }

        Datos = listaDatos.toList()
        notifyItemRemoved(postion)
        notifyDataSetChanged()

    }

    fun actualizarPaciente(nombre: String, uuid: String) {

        GlobalScope.launch(Dispatchers.IO) {
            val objConexion = claseConexion().cadenaConexion()
            val actualizarPaciente = objConexion?.prepareStatement("UPDATE paciente SET nombre = ? WHERE UUID_paciente = ?")!!
            actualizarPaciente.setString(1, nombre)
            actualizarPaciente.setString(2, uuid)
            actualizarPaciente.executeUpdate()

            val commit = objConexion.prepareStatement("COMMIT")
            commit.executeUpdate()

            withContext(Dispatchers.Main) {
                actualizarListaDespuesdeAztualizarlosDatos(uuid, nombre)
            }
        }
    }

    //TODO: Esto ya se ha echo, no se toca
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolde {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card, parent, false)
        return ViewHolde(vista)
    }

    override fun getItemCount() = Datos.size


    override fun onBindViewHolder(holder: ViewHolde, position: Int) {
        val paciente = Datos[position]
        holder.txtNombreCard.text = paciente.nombre
        //TODO: Esto ya se ha echo, no se toca(fin)

        val item = Datos[position]
        holder.imgEliminar.setOnClickListener {

            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)

            builder.setTitle("¿Eliminar?")
            builder.setMessage("¿Seguro de quererlo eliminar?")

            builder.setPositiveButton("Si") { _, _ ->
                eliminarPaciente(item.nombre, position)
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

            val alertDialog = builder.create()
            alertDialog.show()
        }

        holder.imgEditar.setOnClickListener {

            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)

            val cuadritoNuevoPaciente = EditText(context)

            cuadritoNuevoPaciente.setHint(item.nombre)
            builder.setView(cuadritoNuevoPaciente)

            builder.setPositiveButton("Actualizar") { _, _ ->
                actualizarPaciente(cuadritoNuevoPaciente.text.toString(), item.uuidPaciente)
            }

            builder.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }

            val alertDialog = builder.create()
            alertDialog.show()
        }

    }
}