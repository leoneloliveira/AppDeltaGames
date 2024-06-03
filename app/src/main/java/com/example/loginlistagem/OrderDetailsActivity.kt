package com.example.loginlistagem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.NumberFormat
import java.util.Locale

class OrderDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)

        // Recuperar dados passados pela Intent
        val productName = intent.getStringExtra("PRODUCT_NAME")
        val imageUrl = intent.getStringExtra("IMAGE_URL")
        val orderDate = intent.getStringExtra("ORDER_DATE")
        val quantity = intent.getIntExtra("QUANTITY", 0)
        val amount = intent.getDoubleExtra("AMOUNT" , 0.0)
        val address = intent.getStringExtra("ADDRESS")
        val status = intent.getStringExtra("STATUS")

        // Definir os dados nos TextViews
        val productNameTextView = findViewById<TextView>(R.id.productNameTextView)
        productNameTextView.text = productName

        val orderDateTextView = findViewById<TextView>(R.id.orderDateTextView)
        orderDateTextView.text = "Data:   $orderDate"

        val quantityTextView = findViewById<TextView>(R.id.quantityTextView)
        quantityTextView.text = "Quantidade:   $quantity"

        // Formatar o valor do pedido como moeda brasileira
        val format = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        val formattedAmount = format.format(amount)

        val amountTextView = findViewById<TextView>(R.id.amountTextView)
        amountTextView.text = "Valor do Pedido:   $formattedAmount"

        val addressTextView = findViewById<TextView>(R.id.addressTextView)
        addressTextView.text = "Endereço:   $address"

        val statusTextView = findViewById<TextView>(R.id.statusTextView)
        statusTextView.text = "Status:   $status"

        // Carregar a imagem usando Glide
        val orderImageView = findViewById<ImageView>(R.id.orderImageView)
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(orderImageView)

        // Configurar o clique no botão "Comprar Novamente"
        val buyAgainButton = findViewById<Button>(R.id.buyAgainButton)
        buyAgainButton.setOnClickListener {
            // Aqui você cria um Intent para abrir a página de produtos novamente
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this@OrderDetailsActivity, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_orders -> {
                    val intent = Intent(this@OrderDetailsActivity, OrdersActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.btnCarrinho -> {
                    val intent = Intent(this@OrderDetailsActivity, CartActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    val intent = Intent(this@OrderDetailsActivity, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_logout -> {
                    val intent = Intent(this@OrderDetailsActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}
