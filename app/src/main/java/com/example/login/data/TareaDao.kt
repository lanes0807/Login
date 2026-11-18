
package com.example.login.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TareaDao {

    @Insert
    fun insertarTarea(tarea: TareaEntity)

    @Insert
    fun insertarTareas(tareas: List<TareaEntity>)

    @Query("SELECT * FROM tareas WHERE idUsuario = :idUsuario")
    fun obtenerTareas(idUsuario: Int): List<TareaEntity>

    @Query("DELETE FROM tareas WHERE idUsuario = :idUsuario")
    fun eliminarTareasUsuario(idUsuario: Int)
}
