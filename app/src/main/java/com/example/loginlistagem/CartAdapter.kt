package com.example.loginlistagem
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CartAdapter(
    private val items: MutableList<Produto>,
    private val context: Context,
    private val userId: Int,
    private val updateTotal: () -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productName: TextView = view.findViewById(R.id.productNameTextView)
        val productPrice: TextView = view.findViewById(R.id.productPriceTextView)
        val productQuantity: TextView = view.findViewById(R.id.productQuantityTextView)
        val productImage: ImageView = view.findViewById(R.id.productImageView)
        val deleteButton: Button = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detalhe_carrinho, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.productName.text = item.produtoNome
        // CorreÃ§Ã£o: Assegurando que o preÃ§o Ã© tratado como um nÃºmero para formataÃ§Ã£o
        holder.productPrice.text = String.format("R$%.2f", item.produtoPreco.toDouble())
        holder.productQuantity.text = "Qtd: ${item.quantidadeDisponivel}"
        Glide.with(context).load(item.imagemUrl).into(holder.productImage)

        holder.deleteButton.setOnClickListener {
            removeItemFromCart(item, position, userId)
        }
    }

    private fun removeItemFromCart(item: Produto, position: Int, userId: Int) {
        val retrofit = getRetrofit()
        val api = retrofit.create(CartApiService::class.java)
        api.deleteCartItem(item.produtoId, userId = userId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    items.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, items.size)
                    updateTotal()  // Chamada da funÃ§Ã£o para atualizar o total
                    Toast.makeText(context, "Item deletado com sucesso", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Falha ao deletar o item", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "Erro ao conectar-se ao servidor", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://ac4a7aed-781f-4f68-9294-eb5daed5e773-00-3r4vq3nsnj2zx.riker.replit.dev/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun getItemCount() = items.size
}