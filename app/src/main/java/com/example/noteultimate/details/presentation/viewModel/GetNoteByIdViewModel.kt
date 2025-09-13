package com.example.noteultimate.details.presentation.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteultimate.common.Resource
import com.example.noteultimate.details.domain.useCase.GetOneNoteByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetNoteByIdViewModel @Inject constructor(
    private val getOneNoteByIdUseCase: GetOneNoteByIdUseCase,
    savedStateHandle: SavedStateHandle
):ViewModel()
{
    private val _state= MutableStateFlow(GetNoteByIdState())
    var state : StateFlow<GetNoteByIdState> =_state


    init {
        savedStateHandle.get<String>("id")?.let { id ->
            getNoteById(id.toInt())
        }
    }

    fun getNoteById(noteId: Int)=viewModelScope.launch(Dispatchers.IO)
    {
        getOneNoteByIdUseCase.invoke(noteId).collect {

            _state.value = when (it) {
                is Resource.Loading -> GetNoteByIdState(isLoading = true)
                is Resource.Success -> GetNoteByIdState(note = it.data )
                is Resource.Error -> GetNoteByIdState(error = it.message ?: "Unknown error")
            }

            /*when (it) {
                is Resource.Loading -> {
                    _state.value = GetNoteByIdState(isLoading = true)
                }
                is Resource.Success -> {
                    _state.value = GetNoteByIdState(note = it.data )
                }
                is Resource.Error -> {
                    _state.value = GetNoteByIdState(error = it.message ?: "Unknown error")
                }
            }*/

        }
    }
}