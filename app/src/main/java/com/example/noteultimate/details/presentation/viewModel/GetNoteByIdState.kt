package com.example.noteultimate.details.presentation.viewModel

import com.example.noteultimate.home.data.data_sorce.dto.Note


data class GetNoteByIdState(
    val isLoading: Boolean = false,
    val note : Note ?= null,
    val error: String= ""
)
