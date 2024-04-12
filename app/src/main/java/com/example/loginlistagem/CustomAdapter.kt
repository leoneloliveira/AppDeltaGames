package com.example.loginlistagem


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CustomAdapter(private val dataSet: MutableList<Produto>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nome: TextView = view.findViewById(R.id.nomeProduto)
        val descricao: TextView = view.findViewById(R.id.descricaoProduto)
        val valor: TextView = view.findViewById(R.id.valorProduto)
        val imagem: ImageView = view.findViewById(R.id.imagem_produto)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_produto, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val produto = dataSet[position]
        viewHolder.nome.text = produto.produtoNome
        viewHolder.descricao.text = produto.produtoDesc
        viewHolder.valor.text = produto.produtoPreco

        // Carregar imagem usando Glide
        Glide.with(viewHolder.itemView.context)
            .load(produto.imagemUrl)
            .placeholder(R.drawable.ic_launcher_background) // Placeholder enquanto carrega
            .error(com.google.android.material.R.drawable.mtrl_ic_error) // Se houver erro ao carregar
            .into(viewHolder.imagem)
    }

    override fun getItemCount() = dataSet.size

    // Método para filtrar a lista de produtos
    fun filterList(filteredProducts: List<Produto>) {
        dataSet.clear()
        dataSet.addAll(filteredProducts)
        notifyDataSetChanged()
    }
}