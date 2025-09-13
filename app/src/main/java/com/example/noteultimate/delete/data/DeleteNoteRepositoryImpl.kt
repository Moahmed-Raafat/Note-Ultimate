package com.example.noteultimate.delete.data

import com.example.noteultimate.database.NoteDao
import com.example.noteultimate.delete.domain.DeleteNoteRepository
import com.example.noteultimate.home.data.data_sorce.dto.Note
import javax.inject.Inject

class DeleteNoteRepositoryImpl @Inject constructor(private val noteDao: NoteDao): DeleteNoteRepository {

    override suspend fun deleteNote(note: Note) {
        return noteDao.deleteNote(note)
    }

}