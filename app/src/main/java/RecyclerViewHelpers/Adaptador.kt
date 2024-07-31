package RecyclerViewHelpers

import Modelo.paciente
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import rodrigo.monterrosa.bloomhelper.R

class Adaptador (var Datos: List<paciente>): RecyclerView.Adapter<ViewHolde>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolde {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card, parent, false)
        return ViewHolde(vista)
    }

    override fun getItemCount() = Datos.size


    override fun onBindViewHolder(holder: ViewHolde, position: Int) {
        val item = Datos[position]
        holder.txtNombreCard.text = item.nombre

    }

}