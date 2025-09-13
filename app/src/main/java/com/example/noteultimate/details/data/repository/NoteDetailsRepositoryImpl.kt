package com.example.noteultimate.details.data.repository

import com.example.noteultimate.database.NoteDao
import com.example.noteultimate.details.domain.repository.NoteDetailsRepository
import com.example.noteultimate.home.data.data_sorce.dto.Note
import javax.inject.Inject

class NoteDetailsRepositoryImpl @Inject constructor(private val noteDao: NoteDao): NoteDetailsRepository
{
    override suspend fun getNoteById(id: Int): Note {
        return noteDao.getNotesById(id)
    }
}