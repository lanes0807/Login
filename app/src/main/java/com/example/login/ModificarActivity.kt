
package com.example.login

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.login.data.AppDatabase

class ModificarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modificar)

        val txtCorreo = findViewById<EditText>(R.id.txtNuevoCorreo)
        val txtContrasena = findViewById<EditText>(R.id.txtNuevaContrasena)
        val txtConfirmar = findViewById<EditText>(R.id.txtConfirmarContrasena)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarCambios)
        val btnCancelar = findViewById<Button>(R.id.btnCancelarCambios)

        val preferencias = getSharedPreferences("Sesion", MODE_PRIVATE)
        val idUsuario = preferencias.getInt("idUsuario", -1)

        btnGuardar.setOnClickListener {

            val correo = txtCorreo.text.toString().trim()
            val contrasena = txtContrasena.text.toString()
            val confirmar = txtConfirmar.text.toString()

            if (correo.isEmpty() || contrasena.isEmpty() || confirmar.isEmpty()) {

                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()

            } else if (contrasena != confirmar) {

                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()

            } else {

                Thread {

                    val dao = AppDatabase.getInstance(this).userDao()
                    val usuario = dao.buscarPorId(idUsuario)
                    val correoExistente = dao.buscarUsuario(correo)

                    runOnUiThread {

                        if (usuario == null) {

                            Toast.makeText(this, "No se encontró la cuenta", Toast.LENGTH_SHORT).show()

                        } else if (correoExistente != null && correoExistente.id != idUsuario) {

                            Toast.makeText(this, "Este correo ya está registrado", Toast.LENGTH_SHORT).show()

                        } else {

                            Thread {
                                dao.actualizarUsuario(
                                    usuario.copy(
                                        username = correo,
                                        password = contrasena
                                    )
                                )

                                runOnUiThread {
                                    Toast.makeText(this, "Datos actualizados", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                            }.start()
                        }
                    }
                }.start()
            }
        }

        btnCancelar.setOnClickListener {
            finish()
        }
    }
}
