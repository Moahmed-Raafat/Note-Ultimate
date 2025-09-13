package com.example.noteultimate.home.presentation.viewmodel

import com.example.noteultimate.home.data.data_sorce.dto.Note


data class GetNotesState(
    var isLoading:Boolean= false,
    val notes: List<Note> = emptyList(),
    var error:String= ""
)
