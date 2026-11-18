
package com.example.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.login.data.AppDatabase
import com.example.login.data.UserEntity

class CuentaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cuenta)

        val txtNombre = findViewById<EditText>(R.id.txtNombreNuevo)
        val txtCorreo = findViewById<EditText>(R.id.txtCorreoNuevo)
        val txtContrasena = findViewById<EditText>(R.id.txtContrasenaNueva)
        val txtConfirmar = findViewById<EditText>(R.id.txtConfirmar)
        val btnCrear = findViewById<Button>(R.id.btnCrear)

        btnCrear.setOnClickListener {

            val nombre = txtNombre.text.toString().trim()
            val correo = txtCorreo.text.toString().trim()
            val contrasena = txtContrasena.text.toString()
            val confirmar = txtConfirmar.text.toString()

            if (nombre.isEmpty() || correo.isEmpty() ||
                contrasena.isEmpty() || confirmar.isEmpty()) {

                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()

            } else if (contrasena != confirmar) {

                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()

            } else {

                Thread {

                    val dao = AppDatabase.getInstance(this).userDao()
                    val usuarioExistente = dao.buscarUsuario(correo)

                    if (usuarioExistente != null) {

                        runOnUiThread {
                            Toast.makeText(this, "Este correo ya está registrado", Toast.LENGTH_SHORT).show()
                        }

                    } else {

                        val usuario = UserEntity(
                            username = correo,
                            password = contrasena,
                            nombre = nombre
                        )

                        val idUsuario = dao.insertarUsuario(usuario).toInt()

                        runOnUiThread {

                            getSharedPreferences("Sesion", MODE_PRIVATE)
                                .edit()
                                .putInt("idUsuario", idUsuario)
                                .apply()

                            Toast.makeText(this, "Cuenta creada correctamente", Toast.LENGTH_SHORT).show()

                            startActivity(Intent(this, ListaActivity::class.java))
                            finish()
                        }
                    }

                }.start()
            }
        }
    }
}
