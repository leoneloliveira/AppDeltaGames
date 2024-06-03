package com.example.loginlistagem

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Chamar a classe para buscar dados do usuário
        FetchUserData().execute()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this@ProfileActivity, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_orders -> {
                    val intent = Intent(this@ProfileActivity, OrdersActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.btnCarrinho -> {
                    val intent = Intent(this@ProfileActivity, CartActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {

                    true
                }
                R.id.nav_logout -> {
                    val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }

        // Marca o item nav_orders como selecionado
        bottomNavigationView.selectedItemId = R.id.nav_profile
    }

    inner class FetchUserData : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String {
            // Obtém o ID do usuário, substitua isso pelo método correto para obter o ID do usuário logado
            val sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getInt("userId", -1).toString()

            val url = URL("https://92d17040-ccf9-4427-b64f-38f6aa944f00-00-q6dqsgksbx5h.janeway.replit.dev/?user_id=$userId")
            val connection = url.openConnection() as HttpURLConnection
            val response = StringBuilder()

            try {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection.disconnect()
            }

            return response.toString()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()) {
                val userDataArray = JSONArray(result)
                if (userDataArray.length() > 0) {
                    val userData = userDataArray.getJSONObject(0)
                    val userName = userData.getString("USUARIO_NOME")
                    val userEmail = userData.getString("USUARIO_EMAIL")

                    // Atualizar TextViews com os dados do usuário
                    findViewById<TextView>(R.id.userName).text = userName
                    findViewById<TextView>(R.id.userEmail).text = userEmail
                }
            }
        }
    }
}
