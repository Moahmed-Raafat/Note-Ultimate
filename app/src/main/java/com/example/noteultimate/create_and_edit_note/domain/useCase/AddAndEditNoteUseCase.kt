package com.example.noteultimate.create_and_edit_note.domain.useCase


import com.example.noteultimate.common.AppConstants
import com.example.noteultimate.create_and_edit_note.domain.repository.CreateAndEditNoteRepository
import com.example.noteultimate.home.data.data_sorce.dto.InvalidNoteException
import com.example.noteultimate.home.data.data_sorce.dto.Note
import javax.inject.Inject

class AddAndEditNoteUseCase @Inject constructor(private val createAndEditNoteRepository: CreateAndEditNoteRepository)
{
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note)
    {
        if(note.title.isBlank())
        {
            throw InvalidNoteException(AppConstants.ADD_NOTE_NO_TITLE_EXCEPTION_MESSAGE)
        }

        if(note.content.isBlank())
        {
            throw InvalidNoteException(AppConstants.ADD_NOTE_NO_CONTENT_EXCEPTION_MESSAGE)
        }

        createAndEditNoteRepository.addNote(note)
    }
}