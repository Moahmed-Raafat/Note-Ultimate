package com.example.noteultimate.home.domain.use_cases

import com.example.noteultimate.common.Resource
import com.example.noteultimate.home.data.data_sorce.dto.Note
import com.example.noteultimate.home.domain.repository.NotesListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(private val notesListRepository: NotesListRepository)
{
    operator fun invoke(): Flow<Resource<List<Note>>> = flow {

        try {
            emit(Resource.Loading())
            val notesList: List<Note> = notesListRepository.getNotes()
            emit(Resource.Success(notesList))
        }
        catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to get notes"))
        }
    }
}