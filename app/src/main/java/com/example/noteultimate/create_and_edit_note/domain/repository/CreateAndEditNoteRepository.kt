package com.example.noteultimate.create_and_edit_note.domain.repository

import com.example.noteultimate.home.data.data_sorce.dto.Note

interface CreateAndEditNoteRepository {
    suspend fun addNote(note:Note)
}