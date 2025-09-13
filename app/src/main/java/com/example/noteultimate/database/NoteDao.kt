package com.example.noteultimate.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.noteultimate.home.data.data_sorce.dto.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM NOTE")
    fun getNotes(): List<Note>

    @Query("SELECT * FROM NOTE WHERE id=:id")
    suspend fun getNotesById(id : Int):Note

    // onConflict = OnConflictStrategy.REPLACE  means that when you add a record with same id
    // like one in DB just update the existing record
    // so, this function can be used in to add and update a record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note : Note)

    @Delete
    suspend fun deleteNote(note : Note)

}