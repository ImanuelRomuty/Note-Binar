package com.example.notechapter4.room

import androidx.room.*

@Dao
interface NoteDao {
    @Query("SELECT * FROM Note")
    fun getAllNote(): List<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note):Long

    @Update
    fun updateNote(note: Note):Int

    @Delete
    fun deleteNote(note: Note):Int
}