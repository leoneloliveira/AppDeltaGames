package com.example.loginlistagem

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

data class Order(
    val PEDIDO_ID: Int,
    val USUARIO_ID: Int,
    val STATUS_ID: Int,
    val PEDIDO_DATA: String,
    val ENDERECO_ID: Int,
    val ITEM_QTD: Int,
    val ITEM_PRECO: Double,
    val ENDERECO_LOGRADOURO: String,
    val ENDERECO_NUMERO: String,
    val STATUS_DESC: String,
    val PRODUTO_NOME: String,
    val IMAGEM_URL: String
)

class OrdersAdapter(private val orders: List<Order>) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]

        // Converter a data para o formato desejado
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = inputFormat.parse(order.PEDIDO_DATA)
        val formattedDate = outputFormat.format(date)

        // Exibir informações resumidas do pedido
        holder.productNameTextView.text = "Nome : ${order.PRODUTO_NOME}"
        holder.statusTextView.text = "${order.STATUS_DESC}"

        // Calcular e formatar o valor total do pedido como moeda brasileira
        val totalAmount = order.ITEM_QTD * order.ITEM_PRECO
        val format = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        val formattedAmount = format.format(totalAmount)
        holder.amountTextView.text = "Valor do Pedido : $formattedAmount"

        // Configurar o clique no item para abrir a OrderDetailsActivity
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, OrderDetailsActivity::class.java).apply {
                putExtra("PRODUCT_NAME", order.PRODUTO_NOME)
                putExtra("IMAGE_URL", order.IMAGEM_URL)
                putExtra("ORDER_DATE", formattedDate) // Passar a data formatada
                putExtra("QUANTITY", order.ITEM_QTD)
                putExtra("AMOUNT", totalAmount)
                putExtra("ADDRESS", "${order.ENDERECO_LOGRADOURO}, ${order.ENDERECO_NUMERO}")
                putExtra("STATUS", order.STATUS_DESC)
            }
            context.startActivity(intent)
        }

        // Carregar a imagem usando Glide
        Glide.with(holder.itemView)
            .load(order.IMAGEM_URL)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.orderImageView)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderImageView: ImageView = itemView.findViewById(R.id.orderImageView)
        val productNameTextView: TextView = itemView.findViewById(R.id.productNameTextView)
        val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)
        val amountTextView: TextView = itemView.findViewById(R.id.amountTextView)
    }
}
