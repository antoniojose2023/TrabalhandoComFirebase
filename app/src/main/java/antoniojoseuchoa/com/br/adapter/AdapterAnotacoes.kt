package antoniojoseuchoa.com.br.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import antoniojoseuchoa.com.br.databinding.ItemAnotacoesBinding
import antoniojoseuchoa.com.br.model.Anotacao

class AdapterAnotacoes(val context: Context, val list: List<Anotacao>): RecyclerView.Adapter<AdapterAnotacoes.ViewHolder>() {

    var clickDelete: (Anotacao) -> Unit = {}
    var clickUpdate: (Anotacao) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        return ViewHolder(ItemAnotacoesBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.run {
            tvTitleItem.text = item.title
            tvAnotacaoItem.text = item.description

            ivUpdate.setOnClickListener {
                   clickUpdate(item)
            }

            ivDelete.setOnClickListener {
                clickDelete(item)
            }
        }

    }

    override fun getItemCount() = list.size

    class ViewHolder(val binding: ItemAnotacoesBinding): RecyclerView.ViewHolder(binding.root){

    }

}