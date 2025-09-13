package com.example.noteultimate.details.presentation.composables

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.example.noteultimate.details.presentation.viewModel.GetNoteByIdViewModel
import com.example.noteultimate.delete.presentation.DeleteNoteViewModel
import com.example.noteultimate.navigation.Screens


@SuppressLint("UseKtx")
@ExperimentalMaterial3Api
@Composable
fun NoteDetails(navController: NavController,
                getNoteByIdViewModel: GetNoteByIdViewModel = hiltViewModel(),
                deleteNoteViewModel: DeleteNoteViewModel = hiltViewModel()
)
{

    val getNoteByIdState by getNoteByIdViewModel.state.collectAsState()
    val context= LocalContext.current

    LaunchedEffect(getNoteByIdState) {
        if (getNoteByIdState.error.isNotEmpty()) {
            Toast.makeText(context, getNoteByIdState.error, Toast.LENGTH_SHORT).show()
        }
    }

    val scrollState = rememberScrollState()

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
                            text = stringResource(R.string.note_details),
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
        }
    ) { innerPadding ->

        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
            .verticalScroll(scrollState))
        {

            Column (modifier = Modifier.fillMaxSize().padding(5.dp,0.dp))
            {

                if (getNoteByIdState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                getNoteByIdState.note?.let { note ->

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {

                        //edit
                        Text(
                            text = stringResource(R.string.edit),
                            fontSize = 25.sp,
                            modifier = Modifier.padding(10.dp, 0.dp)
                                .clickable {
                                    navController.navigate(Screens.EditNote.route + "/" + note.id.toString())
                                },
                            color = colorResource(R.color.primary)
                        )

                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {

                        // title
                        Text(
                            text = note.title,
                            fontSize = 35.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(10.dp, 0.dp),
                            color = colorResource(R.color.text)
                        )

                    }


                    Spacer(modifier = Modifier.height(20.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

                        //image
                        AsyncImage(
                            model = Uri.parse(note.imageURL),
                            contentDescription = note.title,
                            contentScale = ContentScale.Crop,
                            error = painterResource(R.drawable.image_icon),
                            modifier = Modifier
                                .size(300.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )

                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // content
                    Text(
                        text = note.content,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(10.dp, 0.dp),
                        color = colorResource(R.color.text)
                    )

                    Spacer(modifier = Modifier.height(50.dp))

                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp, 0.dp),
                        horizontalArrangement = Arrangement.SpaceBetween)
                    {

                        //delete
                        Icon(
                            Icons.Rounded.Delete,
                            contentDescription = AppConstants.DELETE_ICON,
                            modifier = Modifier
                                .size(26.dp)
                                .clickable {
                                    deleteNoteViewModel.deleteNote(note)
                                    Toast.makeText(context,R.string.note_is_deleted, Toast.LENGTH_SHORT).show()
                                    navController.navigate(Screens.NoteList.route)
                                },
                            tint = colorResource(R.color.red)
                        )

                        //date
                        Text(
                            text = note.time,
                            fontSize = 15.sp,
                            fontStyle = FontStyle.Italic,
                            color = colorResource(R.color.text)
                        )

                    }
                }

            }

        }
    }

}