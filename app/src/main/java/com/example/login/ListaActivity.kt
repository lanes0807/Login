package com.example.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ListaActivity : AppCompatActivity() {

    data class Tarea(
        var nombre: String,
        var descripcion: String,
        var prioridad: String,
        var estado: String = "Pendiente"
    )

    companion object {
        val tareas = ArrayList<Tarea>()
    }

    var filtroSeleccionado = "Todas"
    var ordenSeleccionado = "Sin ordenar"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        tareas.clear()
        tareas.addAll(Datos.obtenerTareas(this))

        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        val btnMiCuenta = findViewById<Button>(R.id.btnMiCuenta)
        val btnMiPerfil = findViewById<Button>(R.id.btnMiPerfil)

        val txtBuscar = findViewById<EditText>(R.id.txtBuscar)
        val spFiltro = findViewById<Spinner>(R.id.spFiltro)
        val spOrden = findViewById<Spinner>(R.id.spOrden)

        btnAgregar.setOnClickListener {
            val intent = Intent(this, AgregarActivity::class.java)
            startActivity(intent)
        }

        btnMiCuenta.setOnClickListener {
            val intent = Intent(this, ModificarActivity::class.java)
            startActivity(intent)
        }

        btnMiPerfil.setOnClickListener {
            val intent = Intent(this, FotoActivity::class.java)
            startActivity(intent)
        }

        // Opciones para filtrar
        val filtros = arrayOf(
            "Todas",
            "Pendiente",
            "En progreso",
            "Completada"
        )

        val adaptadorFiltro = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            filtros
        )

        adaptadorFiltro.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spFiltro.adapter = adaptadorFiltro

        spFiltro.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    posicion: Int,
                    id: Long
                ) {
                    filtroSeleccionado = filtros[posicion]
                    mostrarTareas()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        // Opciones para ordenar
        val ordenes = arrayOf(
            "Sin ordenar",
            "Prioridad alta a baja",
            "Prioridad baja a alta"
        )

        val adaptadorOrden = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            ordenes
        )

        adaptadorOrden.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spOrden.adapter = adaptadorOrden

        spOrden.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    posicion: Int,
                    id: Long
                ) {
                    ordenSeleccionado = ordenes[posicion]
                    mostrarTareas()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        // Buscar tareas por nombre
        txtBuscar.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                texto: CharSequence?,
                inicio: Int,
                cantidad: Int,
                despues: Int
            ) {
            }

            override fun onTextChanged(
                texto: CharSequence?,
                inicio: Int,
                antes: Int,
                cantidad: Int
            ) {
                mostrarTareas()
            }

            override fun afterTextChanged(texto: Editable?) {
            }
        })
    }

    override fun onResume() {
        super.onResume()
        mostrarTareas()
    }

    fun mostrarTareas() {

        val contenedor = findViewById<LinearLayout>(R.id.contenedorTareas)
        val txtBuscar = findViewById<EditText>(R.id.txtBuscar)

        contenedor.removeAllViews()

        val busqueda = txtBuscar.text.toString().lowercase()

        // Buscar y filtrar
        val listaMostrar = tareas.filter { tarea ->

            val coincideNombre =
                tarea.nombre.lowercase().contains(busqueda)

            val coincideEstado =
                filtroSeleccionado == "Todas" ||
                        tarea.estado == filtroSeleccionado

            coincideNombre && coincideEstado

        }.toMutableList()

        // Ordenar por prioridad
        if (ordenSeleccionado == "Prioridad alta a baja") {
            listaMostrar.sortBy {
                when (it.prioridad) {
                    "Alta" -> 1
                    "Media" -> 2
                    else -> 3
                }
            }
        } else if (ordenSeleccionado == "Prioridad baja a alta") {
            listaMostrar.sortByDescending {
                when (it.prioridad) {
                    "Alta" -> 1
                    "Media" -> 2
                    else -> 3
                }
            }
        }

        for (tarea in listaMostrar) {

            val tarjeta = LinearLayout(this)
            tarjeta.orientation = LinearLayout.VERTICAL
            tarjeta.setBackgroundResource(R.drawable.fondo_tarea)
            tarjeta.setPadding(20, 15, 20, 15)

            val txtNombre = TextView(this)
            txtNombre.text = tarea.nombre
            txtNombre.textSize = 18f
            txtNombre.setTextColor(android.graphics.Color.BLACK)

            val txtDescripcion = TextView(this)
            txtDescripcion.text = tarea.descripcion
            txtDescripcion.textSize = 14f
            txtDescripcion.setPadding(0, 5, 0, 5)

            val txtPrioridad = TextView(this)
            txtPrioridad.text = "Prioridad: " + tarea.prioridad
            txtPrioridad.textSize = 14f

            val txtEstado = TextView(this)
            txtEstado.text = "Estado: " + tarea.estado
            txtEstado.textSize = 14f
            txtEstado.setPadding(0, 5, 0, 5)

            // Cambiar estado
            val spinnerEstado = Spinner(this)

            val estados = arrayOf(
                "Pendiente",
                "En progreso",
                "Completada"
            )

            val adaptadorEstado = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                estados
            )

            adaptadorEstado.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
            )

            spinnerEstado.adapter = adaptadorEstado

            val posicionEstado = estados.indexOf(tarea.estado)

            spinnerEstado.setSelection(
                if (posicionEstado >= 0) posicionEstado else 0
            )

            spinnerEstado.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        posicion: Int,
                        id: Long
                    ) {
                        if (tarea.estado != estados[posicion]) {
                            tarea.estado = estados[posicion]
                            Datos.guardarTareas(this@ListaActivity, tareas)
                            txtEstado.text = "Estado: " + tarea.estado
                            actualizarCantidad()
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }

            // Botón editar
            val btnEditar = Button(this)
            btnEditar.text = "Editar"

            btnEditar.setOnClickListener {

                val posicion = tareas.indexOf(tarea)

                val intent = Intent(this, AgregarActivity::class.java)
                intent.putExtra("posicion", posicion)
                startActivity(intent)
            }

            // Botón eliminar
            val btnEliminar = Button(this)
            btnEliminar.text = "Eliminar"

            btnEliminar.setOnClickListener {

                tareas.remove(tarea)
                Datos.guardarTareas(this, tareas)
                mostrarTareas()

                Toast.makeText(
                    this,
                    "Tarea eliminada",
                    Toast.LENGTH_SHORT
                ).show()
            }

            tarjeta.addView(txtNombre)
            tarjeta.addView(txtDescripcion)
            tarjeta.addView(txtPrioridad)
            tarjeta.addView(txtEstado)
            tarjeta.addView(spinnerEstado)
            tarjeta.addView(btnEditar)
            tarjeta.addView(btnEliminar)

            val parametros = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            parametros.setMargins(0, 0, 0, 15)
            tarjeta.layoutParams = parametros

            contenedor.addView(tarjeta)
        }

        actualizarCantidad()
    }

    fun actualizarCantidad() {

        val txtCantidad = findViewById<TextView>(R.id.txtCantidad)

        var pendientes = 0

        for (tarea in tareas) {
            if (tarea.estado == "Pendiente") {
                pendientes++
            }
        }

        txtCantidad.text = "Tareas pendientes: $pendientes"
    }
}
