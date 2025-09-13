package com.example.noteultimate.create_and_edit_note.data.repository

import com.example.noteultimate.create_and_edit_note.domain.repository.CreateAndEditNoteRepository
import com.example.noteultimate.database.NoteDao
import com.example.noteultimate.home.data.data_sorce.dto.Note
import javax.inject.Inject

class CreateAndEditNoteRepositoryImpl @Inject constructor(private val noteDao: NoteDao): CreateAndEditNoteRepository
{
    override suspend fun addNote(note: Note) {
        return noteDao.insertNote(note)
    }
}