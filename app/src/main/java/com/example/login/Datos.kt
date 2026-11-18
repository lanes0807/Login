
package com.example.login

import android.content.Context
import com.example.login.data.AppDatabase
import com.example.login.data.TareaEntity

object Datos {

    fun guardarCuenta(
        context: Context,
        correo: String,
        contrasena: String
    ) {
        val preferencias = context.getSharedPreferences(
            "Cuenta",
            Context.MODE_PRIVATE
        )

        preferencias.edit()
            .putString("correo", correo)
            .putString("contrasena", contrasena)
            .apply()
    }

    fun obtenerCorreo(context: Context): String {

        val preferencias = context.getSharedPreferences(
            "Cuenta",
            Context.MODE_PRIVATE
        )

        return preferencias.getString("correo", "") ?: ""
    }

    fun obtenerContrasena(context: Context): String {

        val preferencias = context.getSharedPreferences(
            "Cuenta",
            Context.MODE_PRIVATE
        )

        return preferencias.getString("contrasena", "") ?: ""
    }

    fun guardarTareas(
        context: Context,
        tareas: ArrayList<ListaActivity.Tarea>
    ) {

        val preferencias = context.getSharedPreferences(
            "Sesion",
            Context.MODE_PRIVATE
        )

        val idUsuario = preferencias.getInt("idUsuario", -1)

        if (idUsuario == -1) return

        val dao = AppDatabase.getInstance(context).tareaDao()

        dao.eliminarTareasUsuario(idUsuario)

        val listaTareas = tareas.map { tarea ->

            TareaEntity(
                nombre = tarea.nombre,
                descripcion = tarea.descripcion,
                prioridad = tarea.prioridad,
                estado = tarea.estado,
                idUsuario = idUsuario
            )
        }

        dao.insertarTareas(listaTareas)
    }

    fun obtenerTareas(
        context: Context
    ): ArrayList<ListaActivity.Tarea> {

        val tareas = ArrayList<ListaActivity.Tarea>()

        val preferencias = context.getSharedPreferences(
            "Sesion",
            Context.MODE_PRIVATE
        )

        val idUsuario = preferencias.getInt("idUsuario", -1)

        if (idUsuario == -1) return tareas

        val lista = AppDatabase.getInstance(context)
            .tareaDao()
            .obtenerTareas(idUsuario)

        for (tarea in lista) {

            tareas.add(
                ListaActivity.Tarea(
                    tarea.nombre,
                    tarea.descripcion,
                    tarea.prioridad,
                    tarea.estado
                )
            )
        }

        return tareas
    }
}
