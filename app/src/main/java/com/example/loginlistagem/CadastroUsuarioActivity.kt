package com.example.loginlistagem

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.IOException

class CadastroUsuarioActivity : AppCompatActivity() {

    private lateinit var etNome: EditText
    private lateinit var etEmail: EditText
    private lateinit var etSenha: EditText
    private lateinit var etCpf: EditText
    private lateinit var btnCadastrar: Button
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_usuario)

        etNome = findViewById(R.id.etNome)
        etEmail = findViewById(R.id.etEmail)
        etSenha = findViewById(R.id.etSenha)
        etCpf = findViewById(R.id.etCpf)
        btnCadastrar = findViewById(R.id.btnCadastrar)

        btnCadastrar.setOnClickListener {
            val nome = etNome.text.toString()
            val email = etEmail.text.toString()
            val senha = etSenha.text.toString()
            val cpf = etCpf.text.toString()

            if (nome.isNotEmpty() && email.isNotEmpty() && senha.isNotEmpty() && cpf.isNotEmpty()) {
                cadastrarUsuario(nome, email, senha, cpf)
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cadastrarUsuario(nome: String, email: String, senha: String, cpf: String) {
        val url = "https://4b05f4f1-e092-4304-b6ea-a679faf7cb5f-00-24nd7cye1b7yj.spock.replit.dev/"

        val formBody = FormBody.Builder()
            .add("nome", nome)
            .add("email", email)
            .add("senha", senha)
            .add("cpf", cpf)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CadastroUsuarioActivity, "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show()
                        Log.d("CadastroUsuario", "Resposta: $responseBody")

                        // Redirecionar para a tela de login
                        val intent = Intent(this@CadastroUsuarioActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish() // Opcional: para fechar a tela de cadastro de usuário após o redirecionamento
                    }

                    else {
                        Toast.makeText(this@CadastroUsuarioActivity, "Erro ao cadastrar usuário", Toast.LENGTH_SHORT).show()
                        Log.e("CadastroUsuario", "Erro: ${response.message}")
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CadastroUsuarioActivity, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("CadastroUsuario", "Exception: ${e.message}")
                }
            }
        }
    }
}
