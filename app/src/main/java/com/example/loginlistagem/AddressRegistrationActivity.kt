package com.example.loginlistagem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class AddressRegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_registration)

        val etAddressName = findViewById<EditText>(R.id.etAddressName)
        val etAddressStreet = findViewById<EditText>(R.id.etAddressStreet)
        val etAddressNumber = findViewById<EditText>(R.id.etAddressNumber)
        val etAddressComplement = findViewById<EditText>(R.id.etAddressComplement)
        val etAddressCEP = findViewById<EditText>(R.id.etAddressCEP)
        val etAddressCity = findViewById<EditText>(R.id.etAddressCity)
        val etAddressState = findViewById<EditText>(R.id.etAddressState)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        val userName = intent.getStringExtra("USER_NAME")
        val userEmail = intent.getStringExtra("USER_EMAIL")
        val userPassword = intent.getStringExtra("USER_PASSWORD")
        val userCPF = intent.getStringExtra("USER_CPF")

        btnSubmit.setOnClickListener {
            val addressName = etAddressName.text.toString()
            val addressStreet = etAddressStreet.text.toString()
            val addressNumber = etAddressNumber.text.toString()
            val addressComplement = etAddressComplement.text.toString()
            val addressCEP = etAddressCEP.text.toString()
            val addressCity = etAddressCity.text.toString()
            val addressState = etAddressState.text.toString()

            if (addressName.isEmpty() || addressStreet.isEmpty() || addressNumber.isEmpty() ||
                addressCEP.isEmpty() || addressCity.isEmpty() || addressState.isEmpty()) {
                // Display an error message if any of the address fields are empty
                Toast.makeText(this, "Todos os campos  são obrigatórios.", Toast.LENGTH_SHORT).show()
            } else {
                // Create a JSON object with user and address data
                val json = JSONObject()
                json.put("user_name", userName)
                json.put("user_email", userEmail)
                json.put("user_password", userPassword)
                json.put("user_cpf", userCPF)
                json.put("address_name", addressName)
                json.put("address_street", addressStreet)
                json.put("address_number", addressNumber)
                json.put("address_complement", addressComplement)
                json.put("address_cep", addressCEP)
                json.put("address_city", addressCity)
                json.put("address_state", addressState)

                // Send the data to the server
                sendRegistrationData(json.toString())
            }
        }
    }

    private fun sendRegistrationData(jsonData: String) {
        val client = OkHttpClient()
        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = RequestBody.create(mediaType, jsonData)

        val request = Request.Builder()
            .url("https://4b05f4f1-e092-4304-b6ea-a679faf7cb5f-00-24nd7cye1b7yj.spock.replit.dev/")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@AddressRegistrationActivity, "Erro ao registrar. Tente novamente.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        // Registration successful, redirect to login screen
                        Toast.makeText(this@AddressRegistrationActivity, "Cadastro realizado com sucesso.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@AddressRegistrationActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish() // Close the current activity
                    } else {
                        // Registration failed
                        Toast.makeText(this@AddressRegistrationActivity, "Falha no cadastro. Tente novamente.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
