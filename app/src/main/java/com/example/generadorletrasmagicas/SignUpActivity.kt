package com.example.generadorletrasmagicas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.generadorletrasmagicas.databinding.SignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.content.Context
import android.content.SharedPreferences

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding : SignupBinding
    private lateinit var auth: FirebaseAuth
    var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        agregarUsuario()
    }

    private fun agregarUsuario() {
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser
        binding.run{
            buttonContinuar.setOnClickListener{
                val email = editTextEmail.text.toString()
                val password = editTextPassword.text.toString()
                if (validateData(email, password)){
                    createNewUser(email, password)
                    redirectActivity()
                }
            }
            buttonCancelar.setOnClickListener{
                redirectActivity()
            }
        }
    }
    private fun validateData(email: String, password: String): Boolean {
        var valid = true
        if (email.isEmpty()) {
            valid = false
            showMessage("Ingresa un correo")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
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

    /*private fun createNewUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //saveEmail(email)
                    showMessage("Nuevo usuario Creado")
                } else {
                    showMessage("Algo salió mal, intente nuevamente")
                }
            }

    }*/
    private fun createNewUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //saveEmail(email)
                    showMessage("Nuevo usuario Creado")
                } else {
                    val error = task.exception
                    if (error != null) {
                        showMessage("Error: ${error.message}")
                    } else {
                        showMessage("Algo salió mal, intente nuevamente")
                    }
                }
            }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }

    private fun redirectActivity() {
        val intentRedirect = Intent(this, Login::class.java)
        startActivity(intentRedirect)
        finish()
    }



    /*private fun saveEmail(email: String) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.apply()
    }*/

}