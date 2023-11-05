package com.example.generadorletrasmagicas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.generadorletrasmagicas.databinding.LoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Login : AppCompatActivity() {
    private lateinit var binding: LoginBinding
    private lateinit var auth: FirebaseAuth
    var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        iniciarUiLogin()
    }

    private fun iniciarUiLogin() {
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser

        binding.run {
            bt_iniciar_sesion.setOnClickListener {
                val email = email.text.toString()
                val password = password.text.toString()
                if (validateData(email, password)) {
                    loginUser(email, password)
                } else {
                    showMessage("Usuario o contraseña incorrectos")
                }

            }
            createAccount.setOnClickListener {
                redirectCreateActivity()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    redirectActivity()
                } else {
                    showMessage("Usuario o Contraseña incorrectos")
                }
            }
    }

    private fun validateData(email: String, password: String): Boolean {
        var valid = true
        if (email.isEmpty()) {
            valid = false
            showMessage("Ingresa un correo")
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            valid = false
            showMessage("Ingresa un correo válido")
        } else if (password.isEmpty()) {
            valid = false
            showMessage("Ingresa una contraseña")
        } else if (password.length < 8) {
            valid = false
            showMessage("Ingresa una contraseña de al menos 8 dígitos")
        }
        return valid
    }

    private fun showMessage(message: String) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }

    private fun redirectCreateActivity() {
        val intentRedirect = Intent(this, SignUpActivity::class.java)
        startActivity(intentRedirect)
    }

    private fun redirectActivity() {
        val intentRedirect = Intent(this, MainActivity::class.java)
        startActivity(intentRedirect)
//        quita el activity de la pila:
        finish()
    }
}