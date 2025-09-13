package com.example.noteultimate.delete.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteultimate.delete.domain.DeleteNoteUseCase
import com.example.noteultimate.home.data.data_sorce.dto.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteNoteViewModel @Inject constructor(private val deleteNoteUseCase: DeleteNoteUseCase):
    ViewModel()
{
    fun deleteNote(note : Note)= viewModelScope.launch(Dispatchers.IO)
    {
        deleteNoteUseCase.invoke(note)
    }
}