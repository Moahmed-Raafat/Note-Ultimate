package com.example.noteultimate.delete.domain

import com.example.noteultimate.home.data.data_sorce.dto.Note
import com.example.noteultimate.home.domain.repository.NotesListRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(private val deleteNoteRepository: DeleteNoteRepository)
{
    suspend operator fun invoke(note: Note)
    {
        deleteNoteRepository.deleteNote(note)
    }
}