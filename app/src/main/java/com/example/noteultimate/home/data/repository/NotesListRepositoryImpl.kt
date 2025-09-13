package com.example.noteultimate.home.data.repository


import com.example.noteultimate.database.NoteDao
import com.example.noteultimate.home.data.data_sorce.dto.Note
import com.example.noteultimate.home.domain.repository.NotesListRepository
import javax.inject.Inject

class NotesListRepositoryImpl @Inject constructor(private val noteDao: NoteDao): NotesListRepository
{
    override fun getNotes(): List<Note> {
        return noteDao.getNotes()
    }

}