package RecyclerViewHelpers

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import rodrigo.monterrosa.bloomhelper.R

class ViewHolde(View:View): RecyclerView.ViewHolder(View) {

    val txtNombreCard = View.findViewById<TextView>(R.id.txtNombreCard)
    val imgEliminar = View.findViewById<TextView>(R.id.imgEliminar)
    val imgEditar = View.findViewById<TextView>(R.id.imgEditar)

}