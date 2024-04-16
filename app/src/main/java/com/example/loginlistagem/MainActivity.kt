package com.example.loginlistagem


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.SearchView // Adicionado para a funcionalidade de pesquisa
import android.widget.TextView
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CustomAdapter
    private lateinit var produtos: List<Produto> // Lista completa de produtos obtidos da API
    private lateinit var filteredProdutos: MutableList<Produto> // Lista filtrada de produtos exibidos na RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Exibindo o nome do usuário assim que entra na aplicação
        val sharedPreferences = getSharedPreferences("Dados", Context.MODE_PRIVATE)
        val nomeUsuario = sharedPreferences.getString("nome_usuario", "Nome padrão")
        Log.d("MainActivity", "Nome do usuário: $nomeUsuario")
        val nomeUsuarioTextView = findViewById<TextView>(R.id.nomeUsuarioTextView)
        nomeUsuarioTextView.text = "Bem-vindo, $nomeUsuario!" // Aqui você pode customizar a mensagem de boas-vindas


        recyclerView = findViewById(R.id.recyclerViewProdutos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Configuração do Logging Interceptor
        val logging = HttpLoggingInterceptor { message ->
            Log.d("OkHttp", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Configuração do OkHttpClient com o interceptor
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        // Configuração do Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://29160e6c-c252-4b47-8792-6db556195c7f-00-23zckjb72qnun.worf.replit.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService.getProdutos().enqueue(object : Callback<List<Produto>> {
            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                if (response.isSuccessful) {
                    produtos = response.body() ?: emptyList()
                    filteredProdutos = produtos.toMutableList()
                    adapter = CustomAdapter(filteredProdutos)
                    recyclerView.adapter = adapter
                } else {
                    Log.e("API Error", "Response not successful. Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                Log.e("API Failure", "Error fetching products", t)
            }
        })

        // Configuração do SearchView
        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { filter(it) }
                return true
            }
        })


    }


    private fun filter(text: String) {
        filteredProdutos.clear()
        produtos.forEach { produto ->
            if (produto.produtoNome.contains(text, ignoreCase = true)) {
                filteredProdutos.add(produto)
            }
        }
        adapter.notifyDataSetChanged()
    }
}