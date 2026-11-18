
package com.example.login.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    @Insert
    fun insertarUsuario(usuario: UserEntity): Long

    @Query("SELECT * FROM users WHERE username = :nombre LIMIT 1")
    fun buscarUsuario(nombre: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun buscarPorId(id: Int): UserEntity?

    @Update
    fun actualizarUsuario(usuario: UserEntity)

    @Query("SELECT * FROM users ORDER BY id DESC")
    fun getAllUsers(): List<UserEntity>
}
