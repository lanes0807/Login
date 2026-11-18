
package com.example.login

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.login.data.AppDatabase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtCorreo = findViewById<EditText>(R.id.txtCorreo)
        val txtContrasena = findViewById<EditText>(R.id.txtContrasena)
        val btnIniciar = findViewById<Button>(R.id.btnIniciar)
        val txtCrearCuenta = findViewById<TextView>(R.id.txtCrearCuenta)

        btnIniciar.setOnClickListener {

            val correo = txtCorreo.text.toString().trim()
            val contrasena = txtContrasena.text.toString()

            if (correo.isEmpty() || contrasena.isEmpty()) {

                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()

            } else {

                Thread {
                    val usuario = AppDatabase.getInstance(this)
                        .userDao()
                        .buscarUsuario(correo)

                    runOnUiThread {

                        if (usuario != null && usuario.password == contrasena) {

                            getSharedPreferences("Sesion", MODE_PRIVATE)
                                .edit()
                                .putInt("idUsuario", usuario.id)
                                .apply()

                            Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this, ListaActivity::class.java)
                            startActivity(intent)

                        } else {

                            Toast.makeText(
                                this,
                                "Correo o contraseña incorrectos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }.start()
            }
        }

        txtCrearCuenta.setOnClickListener {

            val intent = Intent(this, CuentaActivity::class.java)
            startActivity(intent)

        }
    }
}
