package com.example.loginlistagem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UserRegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_registration)

        val etUserName = findViewById<EditText>(R.id.etUserName)
        val etUserEmail = findViewById<EditText>(R.id.etUserEmail)
        val etUserPassword = findViewById<EditText>(R.id.etUserPassword)
        val etUserCPF = findViewById<EditText>(R.id.etUserCPF)
        val btnNext = findViewById<Button>(R.id.btnNext)

        btnNext.setOnClickListener {
            val userName = etUserName.text.toString()
            val userEmail = etUserEmail.text.toString()
            val userPassword = etUserPassword.text.toString()
            val userCPF = etUserCPF.text.toString()

            if (userName.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty() || userCPF.isEmpty()) {
                // Display an error message if any of the fields are empty
                Toast.makeText(this, "Todos os campos são obrigatórios.", Toast.LENGTH_SHORT).show()
            } else if (!isValidEmail(userEmail)) {
                // Display an error message if the email is not valid
                Toast.makeText(this, "Email inválido. Certifique-se de que contém '@' e '.'.", Toast.LENGTH_SHORT).show()
            } else {
                // Proceed to the next activity if all fields are filled and the email is valid
                val intent = Intent(this, AddressRegistrationActivity::class.java)
                intent.putExtra("USER_NAME", userName)
                intent.putExtra("USER_EMAIL", userEmail)
                intent.putExtra("USER_PASSWORD", userPassword)
                intent.putExtra("USER_CPF", userCPF)
                startActivity(intent)
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }
}
