package com.example.noteultimate.home.presentation.composables

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.noteultimate.R
import com.example.noteultimate.common.AppConstants
import com.example.noteultimate.home.data.data_sorce.dto.Note
import com.example.noteultimate.delete.presentation.DeleteNoteViewModel
import com.example.noteultimate.home.presentation.viewmodel.GetNotesViewModel
import com.example.noteultimate.navigation.Screens
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@ExperimentalMaterial3Api
@Composable
fun NotesList(navController: NavController,
              deleteNoteViewModel: DeleteNoteViewModel = hiltViewModel(),
              noteListViewModel: GetNotesViewModel = hiltViewModel()
              )
{


    val state by noteListViewModel.state.collectAsState()
    val context= LocalContext.current

    var isRefreshing by remember { mutableStateOf(false) }

    var progressBarState by remember { mutableStateOf(false) }

    var showFloatingActionButton by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        when {
            state.isLoading -> {
                progressBarState=true
            }
            state.error.isNotEmpty() -> {
                progressBarState=false
                Toast.makeText(context,state.error, Toast.LENGTH_SHORT).show()
            }
            state.notes.isNotEmpty() -> {
                progressBarState=false
                showFloatingActionButton= true
            }
            else -> {
                showFloatingActionButton= false
                progressBarState=false
            }
        }
    }


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            TopAppBar(
                modifier = Modifier.height(50.dp),
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.home),
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.white),
                            textAlign = TextAlign.Center
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(R.color.primary),
                    titleContentColor = colorResource(R.color.white)
                )
            )
        },
        floatingActionButton = {
            if(showFloatingActionButton)
            {
                FloatingActionButton(onClick = {
                    navController.navigate(Screens.AddAndEditNote.route)
                },
                    contentColor = Color.White,
                    containerColor = colorResource(R.color.primary),
                    modifier = Modifier.testTag(AppConstants.FLOATING_ACTION_BUTTON_OF_NOTES_LIST)) {
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = "add_new_note",
                        modifier = Modifier.size(26.dp) ,
                        tint = colorResource(R.color.white)
                    )
                }
            }

        }
    ) { innerPadding ->

        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(10.dp)
            .fillMaxSize())
        {

            Box(modifier = Modifier.fillMaxSize()) {
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing),
                    onRefresh = {
                        isRefreshing = true
                        noteListViewModel.getNotes()
                    }
                ) {
                    if (state.notes.isEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            item {
                                if (state.isLoading) {
                                    CircularProgressIndicator()
                                }
                                else
                                {
                                    //todo add icon and logic for it
                                    Column(modifier = Modifier
                                        .fillMaxSize()
                                        .padding(5.dp).clickable{
                                            navController.navigate(Screens.AddAndEditNote.route)
                                        },
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    )
                                    {
                                        Icon(
                                            Icons.Rounded.Add,
                                            contentDescription = "add image",
                                            modifier = Modifier.size(30.dp),
                                            tint = colorResource(R.color.text)
                                        )
                                        Text("Looks empty here. Write something down!",
                                            color = colorResource(R.color.text))
                                    }


                                }
                            }
                        }
                    }
                    else
                    {
                        ShowList(navController,state.notes,deleteNoteViewModel,noteListViewModel,context)
                    }

                    isRefreshing = false
                }
            }

        }
    }

}

@SuppressLint("UseKtx")
@Composable
private fun ShowList(navController: NavController,
                     list:List<Note>,
                     deleteNoteViewModel:DeleteNoteViewModel,
                     noteListViewModel: GetNotesViewModel,
                     context: Context)
{
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {

        itemsIndexed(list)
        {index, item ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        navController.navigate(Screens.NoteDetails.route + "/" + item.id.toString())
                    },
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            )
            {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp))
                {

                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween)
                    {

                        Column {

                            //title
                            val titleMaxChars = 15
                            val displayTitle = if (item.title.length > titleMaxChars) {
                                item.title.take(titleMaxChars) + "..."
                            } else {
                                item.title
                            }
                            Text(text = displayTitle,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(R.color.text))

                            Spacer(modifier = Modifier
                                .height(10.dp))

                            //content
                            val contentMaxChars = 30
                            val displayContent = if (item.content.length > contentMaxChars) {
                                item.content.take(contentMaxChars) + "..."
                            } else {
                                item.content
                            }
                            Text(text = displayContent,
                                fontSize = 18.sp,
                                color = colorResource(R.color.text))

                        }

                        //image
                        AsyncImage(
                            model = Uri.parse(item.imageURL),
                            contentDescription = item.title,
                            contentScale = ContentScale.Crop,
                            error = painterResource(R.drawable.image_icon),
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )


                    }

                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp))


                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween)
                    {

                        //delete
                        Icon(
                            Icons.Rounded.Delete,
                            contentDescription = AppConstants.DELETE_ICON,
                            modifier = Modifier
                                .size(26.dp)
                                .clickable {
                                    deleteNoteViewModel.deleteNote(item)
                                    Toast.makeText(context,"Note is deleted", Toast.LENGTH_SHORT).show()
                                    noteListViewModel.getNotes()
                                },
                            tint = Color.Black
                        )

                        Row {
                            //date
                            Text(
                                text = item.time,
                                fontSize = 12.sp,
                                fontStyle = FontStyle.Italic,
                                color = colorResource(R.color.text)
                            )


                            //update flag
                            if(item.isUpdated)
                            {
                                Spacer(modifier = Modifier
                                    .fillMaxHeight()
                                    .width(10.dp))

                                Image(
                                    painter = painterResource(id = R.drawable.updated_icon),
                                    contentDescription = "updated",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }





                    }
                }
            }
        }
    }
}

