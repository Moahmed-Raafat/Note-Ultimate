package com.example.noteultimate.home.data.data_sorce.dto
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(

    val title : String,
    val content : String,
    val time:String,
    val imageURL : String,

    val isUpdated: Boolean = false,

    @PrimaryKey
    val id : Int ?=null

)

class InvalidNoteException(message:String): Exception(message)
