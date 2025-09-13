package com.example.noteultimate.create_and_edit_note.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteultimate.home.data.data_sorce.dto.InvalidNoteException
import com.example.noteultimate.home.data.data_sorce.dto.Note
import com.example.noteultimate.create_and_edit_note.domain.useCase.AddAndEditNoteUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAndEditNoteViewModel @Inject constructor(private val addNotesUseCase: AddAndEditNoteUseCase):ViewModel()
{
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isNoteAdded = MutableStateFlow(false)
    val isNoteAdded: StateFlow<Boolean> = _isNoteAdded

    fun addNote(note : Note)=viewModelScope.launch(Dispatchers.IO)
    {
        try
        {
            addNotesUseCase.invoke(note)
            _errorMessage.value = null
            _isNoteAdded.value = true
        }
        catch (e: InvalidNoteException)
        {
            _errorMessage.value = e.message
            _isNoteAdded.value = false
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun resetNoteAddedState() {
        _isNoteAdded.value = false
    }
}