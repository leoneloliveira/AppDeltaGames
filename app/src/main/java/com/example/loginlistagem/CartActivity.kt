package com.example.loginlistagem


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CartActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var totalTextView: TextView
    private lateinit var goToPaymentButton: Button
    private var total: Double = 0.0
    private lateinit var cartAdapter: CartAdapter
    private var items: MutableList<Produto> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("userId", 0)

        recyclerView = findViewById(R.id.cartRecyclerView)
        totalTextView = findViewById(R.id.totalTextView)
        goToPaymentButton = findViewById(R.id.goToPaymentButton)

        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchCartItems()


        goToPaymentButton.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java).apply {
                var value=updateTotal()
                putExtra("TOTAL", total.toString())
                putExtra("USER", userId)
                putParcelableArrayListExtra("PRODUCT_LIST", ArrayList(items))
            }
            startActivity(intent)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this@CartActivity, MainActivity::class.java)
                    intent.putExtra("userId", userId)
                    startActivity(intent)
                    true
                }
                R.id.nav_orders -> {
                    val intent = Intent(this@CartActivity, OrdersActivity::class.java)
                    intent.putExtra("userId", userId)
                    startActivity(intent)
                    true
                }
                R.id.btnCarrinho -> {

                    true
                }
                R.id.nav_profile -> {
                    val intent = Intent(this@CartActivity, ProfileActivity::class.java)
                    intent.putExtra("userId", userId)
                    startActivity(intent)
                    true
                }
                R.id.nav_logout -> {
                    val intent = Intent(this@CartActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }

        // Marca o item nav_orders como selecionado
        bottomNavigationView.selectedItemId = R.id.btnCarrinho
    }


    private fun fetchCartItems() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://ac4a7aed-781f-4f68-9294-eb5daed5e773-00-3r4vq3nsnj2zx.riker.replit.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(CartApiService::class.java)

        val sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("userId", 0)

        api.getCartItems(userId).enqueue(object : Callback<List<Produto>> {
            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                if (response.isSuccessful) {
                    // Filtra os itens com quantidadeDisponivel >= 1
                    items = response.body()?.filter { it.quantidadeDisponivel >= 1 }?.toMutableList() ?: mutableListOf()
                    setupAdapter()
                    updateTotal()
                }
            }

            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                // Tratamento de falhas
            }
        })
    }

    private fun setupAdapter() {
        cartAdapter = CartAdapter(items, this) { updateTotal() }
        recyclerView.adapter = cartAdapter
    }

    fun updateTotal() {
        total = items.sumOf { (it.produtoPreco?.toDouble() ?: 0.0) * it.quantidadeDisponivel.toDouble() }
        runOnUiThread {
            totalTextView.text = "Total: R$${String.format("%.2f", total)}"
        }
    }
}