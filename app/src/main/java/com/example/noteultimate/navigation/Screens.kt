package com.example.noteultimate.navigation

sealed class Screens (val route:String)
{
    data object NoteList:Screens("note_list_screen")
    data object AddAndEditNote:Screens("add_and_edit_note_screen")
    data object NoteDetails:Screens("note_details_screen")
    data object EditNote:Screens("edit_note")
}