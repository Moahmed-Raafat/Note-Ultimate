package com.example.noteultimate.details.domain.useCase

import com.example.noteultimate.common.Resource
import com.example.noteultimate.details.domain.repository.NoteDetailsRepository
import com.example.noteultimate.home.data.data_sorce.dto.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetOneNoteByIdUseCase @Inject constructor(private val noteDetailsRepository: NoteDetailsRepository)
{

    operator fun invoke(noteId: Int): Flow<Resource<Note>> = flow {

        try {
            emit(Resource.Loading())
            val note: Note = noteDetailsRepository.getNoteById(noteId)
            emit(Resource.Success(note))
        }
        catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to get notes"))
        }
    }
}