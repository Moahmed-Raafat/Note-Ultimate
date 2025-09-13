package com.example.noteultimate.home.domain.repository

import com.example.noteultimate.home.data.data_sorce.dto.Note

interface NotesListRepository {

    fun getNotes(): List<Note>
}