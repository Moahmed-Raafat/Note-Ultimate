package com.example.noteultimate.details.domain.repository

import com.example.noteultimate.home.data.data_sorce.dto.Note

interface NoteDetailsRepository {
    suspend fun getNoteById(id:Int) : Note
}