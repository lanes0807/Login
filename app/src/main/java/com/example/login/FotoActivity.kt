
package com.example.login

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.login.data.AppDatabase

class FotoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foto)

        val txtUsuario = findViewById<TextView>(R.id.txtUsuario)
        val btnRegresar = findViewById<Button>(R.id.btnRegresar)
        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)

        val idUsuario = getSharedPreferences("Sesion", MODE_PRIVATE)
            .getInt("idUsuario", -1)

        Thread {
            val usuario = AppDatabase.getInstance(this)
                .userDao()
                .buscarPorId(idUsuario)

            runOnUiThread {
                if (usuario != null) {
                    txtUsuario.text = "Nombre: ${usuario.nombre}\nCorreo: ${usuario.username}"
                } else {
                    txtUsuario.text = "No se encontró la cuenta"
                }
            }
        }.start()

        btnRegresar.setOnClickListener {
            finish()
        }

        btnCerrarSesion.setOnClickListener {

            getSharedPreferences("Sesion", MODE_PRIVATE)
                .edit()
                .remove("idUsuario")
                .apply()

            val intent = android.content.Intent(this, MainActivity::class.java)
            intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or
                    android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
            finish()
        }
    }
}
