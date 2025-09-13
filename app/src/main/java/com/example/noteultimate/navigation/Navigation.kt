package com.example.noteultimate.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.noteultimate.create_and_edit_note.presentation.composables.AddNote
import com.example.noteultimate.details.presentation.composables.NoteDetails
import com.example.noteultimate.create_and_edit_note.presentation.composables.EditNote
import com.example.noteultimate.home.presentation.composables.NotesList

@ExperimentalMaterial3Api
@Composable
fun Navigation()
{
    val navController= rememberNavController()
    NavHost(navController = navController, startDestination = Screens.NoteList.route)
    {

        composable(route=Screens.NoteList.route)
        {
            NotesList(navController = navController)
        }
        composable(route=Screens.AddAndEditNote.route)
        {
            AddNote(navController = navController)
        }
        composable(route=Screens.NoteDetails.route+"/{id}")
        {
            NoteDetails(navController = navController)
        }
        composable(route=Screens.EditNote.route+"/{id}")
        {
            EditNote(navController = navController)
        }
    }
}