package com.example.notechapter4.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM User WHERE email = :email AND password = :password")
    fun login(email: String, password: String):User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserAccount(user: User):Long
}