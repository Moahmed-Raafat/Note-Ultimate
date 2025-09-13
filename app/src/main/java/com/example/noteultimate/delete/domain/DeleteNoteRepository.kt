package com.example.noteultimate.delete.domain

import com.example.noteultimate.home.data.data_sorce.dto.Note

interface DeleteNoteRepository {
    suspend fun deleteNote(note: Note)
}