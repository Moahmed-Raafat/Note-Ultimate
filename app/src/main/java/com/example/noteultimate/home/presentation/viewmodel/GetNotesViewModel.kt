package com.example.noteultimate.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteultimate.common.Resource
import com.example.noteultimate.home.domain.use_cases.GetNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetNotesViewModel @Inject constructor(private val getNotesUseCase: GetNotesUseCase):ViewModel()
{
    private val _state= MutableStateFlow(GetNotesState())
    var state : StateFlow<GetNotesState> =_state

    init {
        getNotes()
    }

    fun getNotes()=viewModelScope.launch(Dispatchers.IO)
    {
        getNotesUseCase.invoke().collect {

            when (it) {
                is Resource.Loading -> {
                    _state.value = GetNotesState(isLoading = true)
                }
                is Resource.Success -> {
                    _state.value = GetNotesState(notes = it.data ?: emptyList())
                }
                is Resource.Error -> {
                    _state.value = GetNotesState(error = it.message ?: "Unknown error")
                }
            }

        }
    }

}