package com.example.loginlistagem

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class ProdutoDetalhesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produto_detalhes)

        val nomeProduto = intent.getStringExtra("NOME_PRODUTO") ?: "Nome nÃƒÂ£o disponÃƒÂ­vel"
        val descricaoProduto =
            intent.getStringExtra("DESCRICAO_PRODUTO") ?: "DescriÃƒÂ§ÃƒÂ£o nÃƒÂ£o disponÃƒÂ­vel"
        val produtoId = intent.getIntExtra("ID_PRODUTO", 0)
        val quantidadeDisponivel = intent.getIntExtra("QUANTIDADE_DISPONIVEL", 0)
        val imagemProguto = intent.getStringExtra("IMAGEM_URL") ?: "imagem indisponivel"

        Glide.with(this)
            .load(imagemProguto)
            .placeholder(R.drawable.ic_launcher_background) // placeholder
            .error(com.google.android.material.R.drawable.mtrl_ic_error) // indica erro
            .into(findViewById<ImageView>(R.id.imagem_produto))

        findViewById<TextView>(R.id.txtNomeProduto).text = nomeProduto
        findViewById<TextView>(R.id.txtDescricaoProduto).text = descricaoProduto
        findViewById<TextView>(R.id.txtQuantidadeDisponivel).text = quantidadeDisponivel.toString()

        val editTextQuantidade = findViewById<EditText>(R.id.editQuantidadeDesejada)
        val btnAdicionarCarrinho = findViewById<ImageView>(R.id.btnAdicionarAoCarrinho)


        val sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("userId", 0)



        btnAdicionarCarrinho.setOnClickListener {
            val quantidadeDesejada = editTextQuantidade.text.toString().toIntOrNull() ?: 0
            adicionarAoCarrinho(userId, produtoId, quantidadeDesejada)
        }


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this@ProdutoDetalhesActivity, MainActivity::class.java)
                    intent.putExtra("userId", userId)
                    startActivity(intent)
                    true
                }

                R.id.nav_orders -> {
                    val intent = Intent(this@ProdutoDetalhesActivity, OrdersActivity::class.java)
                    intent.putExtra("userId", userId)
                    startActivity(intent)
                    true
                }

                R.id.btnCarrinho -> {
                    val intent = Intent(this@ProdutoDetalhesActivity, CartActivity::class.java)
                    intent.putExtra("userId", userId)
                    startActivity(intent)
                    true
                }

                R.id.nav_profile -> {
                    val intent = Intent(this@ProdutoDetalhesActivity, ProfileActivity::class.java)
                    intent.putExtra("userId", userId)
                    startActivity(intent)
                    true
                }

                R.id.nav_logout -> {
                    val intent = Intent(this@ProdutoDetalhesActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                    true
                }

                else -> false
            }
        }





    }

    private fun adicionarAoCarrinho(userId: Int, produtoId: Int, quantidade: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://5267dfa7-8c8c-422d-bc0f-9f07d85896c0-00-32vrx86wdz0zu.worf.replit.dev/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)
        api.adicionarAoCarrinho(userId, produtoId, quantidade).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ProdutoDetalhesActivity, response.body() ?: "Sucesso!", Toast.LENGTH_SHORT).show()
                    // Redireciona para a página do carrinho
                    val intent = Intent(this@ProdutoDetalhesActivity, CartActivity::class.java)
                    startActivity(intent)

                } else {
                    Toast.makeText(this@ProdutoDetalhesActivity, "Resposta nÃƒÂ£o bem-sucedida", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@ProdutoDetalhesActivity, "Erro na API: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    interface ApiService {
        @FormUrlEncoded
        @POST("/")
        fun adicionarAoCarrinho(
            @Field("userId") userId: Int,
            @Field("produtoId") produtoId: Int,
            @Field("quantidade") quantidade: Int
        ): Call<String>
    }
}





