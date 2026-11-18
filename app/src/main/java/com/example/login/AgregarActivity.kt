
package com.example.login

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AgregarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar)

        val txtNombreTarea = findViewById<EditText>(R.id.txtNombreTarea)
        val txtDescripcion = findViewById<EditText>(R.id.txtDescripcion)
        val grupoPrioridad = findViewById<RadioGroup>(R.id.grupoPrioridad)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)

        val posicion = intent.getIntExtra("posicion", -1)

        // Mostrar los datos si estamos editando
        if (posicion in ListaActivity.tareas.indices) {

            val tarea = ListaActivity.tareas[posicion]

            txtNombreTarea.setText(tarea.nombre)
            txtDescripcion.setText(tarea.descripcion)

            if (tarea.prioridad == "Alta") {
                grupoPrioridad.check(R.id.rbAlta)
            } else if (tarea.prioridad == "Media") {
                grupoPrioridad.check(R.id.rbMedia)
            } else if (tarea.prioridad == "Baja") {
                grupoPrioridad.check(R.id.rbBaja)
            }

            btnGuardar.text = "Guardar cambios"
        }

        btnGuardar.setOnClickListener {

            val nombre = txtNombreTarea.text.toString().trim()
            val descripcion = txtDescripcion.text.toString().trim()
            val prioridad = grupoPrioridad.checkedRadioButtonId

            if (nombre.isEmpty() || descripcion.isEmpty() || prioridad == -1) {

                Toast.makeText(
                    this,
                    "Completa todos los campos",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                var prioridadTexto = ""

                if (prioridad == R.id.rbAlta) {
                    prioridadTexto = "Alta"
                } else if (prioridad == R.id.rbMedia) {
                    prioridadTexto = "Media"
                } else if (prioridad == R.id.rbBaja) {
                    prioridadTexto = "Baja"
                }

                if (posicion in ListaActivity.tareas.indices) {

                    val tarea = ListaActivity.tareas[posicion]

                    tarea.nombre = nombre
                    tarea.descripcion = descripcion
                    tarea.prioridad = prioridadTexto

                    Toast.makeText(
                        this,
                        "Tarea modificada",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    val nuevaTarea = ListaActivity.Tarea(
                        nombre,
                        descripcion,
                        prioridadTexto
                    )

                    ListaActivity.tareas.add(nuevaTarea)

                    Toast.makeText(
                        this,
                        "Tarea guardada",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                Datos.guardarTareas(this, ListaActivity.tareas)
                finish()
            }
        }

        btnCancelar.setOnClickListener {
            finish()
        }
    }
}
