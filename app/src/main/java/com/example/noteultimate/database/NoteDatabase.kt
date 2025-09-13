package com.example.noteultimate.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.noteultimate.home.data.data_sorce.dto.Note

@Database(
    entities = [Note::class],
    version = 1)
abstract class NoteDatabase: RoomDatabase()
{
    abstract val noteDao: NoteDao

    companion object{

        //name of the database
        const val DATABASE_NAME = "notes_db"
    }
}