package com.example.noteultimate.di

import android.app.Application
import androidx.room.Room
import com.example.noteultimate.create_and_edit_note.data.repository.CreateAndEditNoteRepositoryImpl
import com.example.noteultimate.create_and_edit_note.domain.repository.CreateAndEditNoteRepository

import com.example.noteultimate.database.NoteDatabase
import com.example.noteultimate.delete.data.DeleteNoteRepositoryImpl
import com.example.noteultimate.delete.domain.DeleteNoteRepository
import com.example.noteultimate.details.data.repository.NoteDetailsRepositoryImpl
import com.example.noteultimate.details.domain.repository.NoteDetailsRepository
import com.example.noteultimate.home.data.repository.NotesListRepositoryImpl
import com.example.noteultimate.home.domain.repository.NotesListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDataBase(app: Application): NoteDatabase
    {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db : NoteDatabase) : NotesListRepository
    {
        return NotesListRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteDetailsRepository(db : NoteDatabase) : NoteDetailsRepository
    {
        return NoteDetailsRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideDeleteNoteRepository(db : NoteDatabase) : DeleteNoteRepository
    {
        return DeleteNoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideCreateAndEditNoteRepository(db : NoteDatabase) : CreateAndEditNoteRepository
    {
        return CreateAndEditNoteRepositoryImpl(db.noteDao)
    }


}