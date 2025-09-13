package com.example.noteultimate.create_and_edit_note.presentation.composables

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.noteultimate.R
import com.example.noteultimate.create_and_edit_note.CloudinaryUploader.initCloudinary
import com.example.noteultimate.create_and_edit_note.CloudinaryUploader.uploadImageToCloudinary
import com.example.noteultimate.create_and_edit_note.presentation.viewModel.AddAndEditNoteViewModel
import com.example.noteultimate.details.presentation.viewModel.GetNoteByIdViewModel
import com.example.noteultimate.home.data.data_sorce.dto.Note
import com.example.noteultimate.navigation.Screens
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@SuppressLint("AutoboxingStateCreation")
@ExperimentalMaterial3Api
@Composable
fun EditNote(
    navController: NavController,
    viewModel: AddAndEditNoteViewModel = hiltViewModel(),
    getNoteByIdViewModel: GetNoteByIdViewModel = hiltViewModel())
{

    val getNoteByIdState by getNoteByIdViewModel.state.collectAsState()
    val context = LocalContext.current

    //title state
    var idState: Int? by remember { mutableStateOf(-1) }

    //title state
    var titleState by remember { mutableStateOf("") }

    //content state
    var contentState by remember { mutableStateOf("") }

    //date state
    var dateState by remember { mutableStateOf("") }

    // Observe error messages from ViewModel
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Observe success state from ViewModel
    val isNoteAdded by viewModel.isNoteAdded.collectAsState()

    var progressBarState by remember { mutableStateOf(false) }
    var imageButtonState by remember { mutableStateOf(true) }
    var buttonState by remember { mutableStateOf(true) }

    //image
    var showImageUploader by remember { mutableStateOf(false) }
    var uploadedImageUrl by remember { mutableStateOf<String?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    //disable the button when an image is getting uploaded
    LaunchedEffect(isUploading) {
        buttonState = !isUploading
    }

    // Show Toast when errorMessage changes
    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

            buttonState=true

            // Clear the error message after showing the Toast
            viewModel.clearErrorMessage()
        }
    }

    //showing note details
    LaunchedEffect(getNoteByIdState)
    {
        when {
            getNoteByIdState.isLoading -> {
                progressBarState = true
            }

            getNoteByIdState.error.isNotEmpty() -> {
                progressBarState = false
                Toast.makeText(context, getNoteByIdState.error, Toast.LENGTH_SHORT).show()
            }

            getNoteByIdState.note != null -> {
                progressBarState = false

                val note = getNoteByIdState.note
                idState= note?.id
                titleState = note?.title ?: ""
                contentState = note?.content ?: ""
                uploadedImageUrl = note?.imageURL
                dateState= note?.time ?: ""
            }
        }
    }

    // Handle successful note editing
    LaunchedEffect(isNoteAdded) {
        if (isNoteAdded) {

            // Navigate to the NoteList screen
            navController.navigate(Screens.NoteList.route){
                popUpTo(0)
            }

            buttonState = true

            // Show success Toast
            Toast.makeText(context, "Note is added successfully", Toast.LENGTH_SHORT).show()

            // Reset the success state
            viewModel.resetNoteAddedState()
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
                            text = stringResource(R.string.edit_note),
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

        Column(
            modifier = Modifier.padding(innerPadding).padding(horizontal = 15.dp)
                .fillMaxSize()
        )
        {

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
            )

            //title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, 0.dp),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                OutlinedTextField(
                    value = titleState,
                    label = { Text(text = "enter title") },
                    onValueChange = { titleState = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            )

            //content
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                OutlinedTextField(
                    value = contentState,
                    label = { Text(text = "enter content") },
                    onValueChange = { contentState = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            )

            //image
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically)
            {
                Card(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .border(
                                border = BorderStroke(.5.dp, colorResource(id = R.color.black)),
                                shape = RoundedCornerShape(5.dp)
                            ),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(id = R.color.surface_card)
                    )
                )
                {
                    //upload an image
                    if(uploadedImageUrl.isNullOrEmpty())
                    {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(13.dp)
                                .clickable {
                                    if(imageButtonState)
                                    {
                                        //capture an image and upload it to cloudinary
                                        showImageUploader = true
                                        imageButtonState = false
                                    }
                                },
                            contentAlignment = Alignment.Center
                        )
                        {
                            Column (modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally){
                                Icon(
                                    Icons.Rounded.Add,
                                    contentDescription = "add image",
                                    modifier = Modifier.size(50.dp),
                                    tint = Color.Black
                                )
                                Text(text = stringResource(id = R.string.add_image),
                                    fontSize = 25.sp,
                                    fontStyle = FontStyle.Italic)
                            }

                        }
                    }
                    //showing the image
                    else{
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .padding(10.dp)
                                .clickable {
                                    if(imageButtonState)
                                    {
                                        // Allow re-selecting or replacing the image
                                        showImageUploader = true
                                        imageButtonState = false
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = uploadedImageUrl,
                                contentDescription = "Uploaded Image",
                                contentScale = ContentScale.Crop,
                                error = painterResource(R.drawable.image_icon),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(10.dp))
                            )
                        }
                    }

                }
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            )

            //date
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.End
            )
            {
                Text(
                    text = dateState,
                    fontSize = 15.sp,
                    fontStyle = FontStyle.Italic,
                    color = colorResource(R.color.text)
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
            )

            //save
            Row (modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically)
            {

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp),
                    enabled = buttonState,

                    onClick = {

                        if (!buttonState) return@Button
                        val note = Note(
                            title = titleState,
                            content= contentState,
                            time= getCurrentDate(),
                            imageURL =  uploadedImageUrl.toString(),
                            id =  idState,
                            isUpdated = true)

                        viewModel.addNote(note)
                        buttonState=false

                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = colorResource(id = R.color.white),
                        containerColor = colorResource(id = R.color.primary)
                    ),
                    shape = RoundedCornerShape(5.dp)
                )
                {
                    Text(text = "save", fontSize = 20.sp)
                }
            }

            if (progressBarState || isUploading) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                )
                {
                    CircularProgressIndicator()
                }
            }
        }
    }

    if (showImageUploader) {
        ImageUploaderScreenImage(
            onUploadStarted = { isUploading = true },
            onUploadFinished = { url ->
                uploadedImageUrl = url
                isUploading = false
                showImageUploader = false
                imageButtonState=true
            },
            onDismissRequest = { showImageUploader = false }
        )
    }

}

private fun getCurrentDate():String
{
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return current.format(formatter)
}


@Composable
fun ImageUploaderScreenImage(
    onUploadStarted: () -> Unit,
    onUploadFinished: (String?) -> Unit,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    // Initialize Cloudinary once
    LaunchedEffect(Unit) {
        initCloudinary(context)
    }

    // 1. Launcher for Gallery Selection
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            onDismissRequest()

            onUploadStarted()
            uploadImageToCloudinary(context, it) { url ->
                onUploadFinished(url)

            }
        }?: onDismissRequest()
    }


    // 2. Launcher for Taking Picture
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            tempImageUri?.let { uri ->

                onDismissRequest()

                onUploadStarted()

                // Compress and upload
                val compressedFile = compressImageEdit(context, uri)
                val compressedUri = Uri.fromFile(compressedFile)
                uploadImageToCloudinary(context, compressedUri) { url ->
                    onUploadFinished(url)
                    // Clean up temp files
                    File(uri.path ?: "").delete()
                    compressedFile.delete()
                }
            }
        }
        else
        {
            onDismissRequest()
        }
    }


    // 3. Permission Launcher for CAMERA
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                val file = File(
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "camera_image_${System.currentTimeMillis()}.jpg"
                )
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    file
                )
                tempImageUri = uri
                cameraLauncher.launch(uri)
            } else {
                Toast.makeText(context,R.string.camera_permission_is_required, Toast.LENGTH_SHORT).show()
            }
        }
    )


    // 4. Alert Dialog for selecting camera or gallery
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text= stringResource(R.string.upload_image)) },
        text = { Text(text = stringResource(R.string.choose_an_image_from_the_gallery_or_take_a_new_photo_)) },
        confirmButton = {
            TextButton(onClick = {
                //onDismissRequest() // Dismiss the dialog
                galleryLauncher.launch("image/*")
            }) {
                Text(text = stringResource(R.string.gallery))
            }
        },
        dismissButton = {
            TextButton(onClick = {
                //onDismissRequest() // Dismiss the dialog
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }) {
                Text(text = stringResource(R.string.camera))
            }
        }
    )
}

@SuppressLint("UseKtx")
fun compressImageEdit(context: Context, imageUri: Uri): File {
    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)

    // Resize
    val resized = Bitmap.createScaledBitmap(bitmap, 500, 500, true)

    // Compress
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val storageDir = context.cacheDir
    val compressedFile = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    val outputStream = FileOutputStream(compressedFile)
    resized.compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    outputStream.flush()
    outputStream.close()

    return compressedFile
}


