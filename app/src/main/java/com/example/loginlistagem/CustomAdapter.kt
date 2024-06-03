package com.example.loginlistagem

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CustomAdapter(private val dataSet: List<Produto>)
    :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nome: TextView = view.findViewById(R.id.nomeProduto)

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

        // Formatação explícita do valor com símbolo de moeda
        val formattedPrice = "R$ %.2f".format(produto.produtoPreco)
        viewHolder.valor.text = formattedPrice

        Glide.with(viewHolder.itemView.context)
            .load(produto.imagemUrl)
            .placeholder(R.drawable.ic_launcher_background) // placeholder
            .error(com.google.android.material.R.drawable.mtrl_ic_error) // indica erro
            .into(viewHolder.imagem)

        // Defina o OnClickListener no itemView (todo o layout do item)
        viewHolder.itemView.setOnClickListener {
            val intent = Intent(viewHolder.itemView.context, ProdutoDetalhesActivity::class.java).apply {
                putExtra("ID_PRODUTO", produto.produtoId)
                putExtra("NOME_PRODUTO", produto.produtoNome)
                putExtra("IMAGEM_URL", produto.imagemUrl)
                putExtra("DESCRICAO_PRODUTO", produto.produtoDesc)
                putExtra("QUANTIDADE_DISPONIVEL", produto.quantidadeDisponivel)
            }
            viewHolder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = dataSet.size
}
