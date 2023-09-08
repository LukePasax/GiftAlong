package com.giacomosirri.myapplication.ui.navigation

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.giacomosirri.myapplication.R
import com.giacomosirri.myapplication.ui.AppContext
import com.giacomosirri.myapplication.viewmodel.AppViewModel
import kotlinx.coroutines.runBlocking

@Composable
fun ChangeProfilePicScreen(
    paddingValues: PaddingValues,
    viewModel: AppViewModel
) {
    Scaffold(
        topBar = {
            NavigationAppBar(
                currentScreenName = AppContext.getContext()?.getString(R.string.title_new_profile_pic)!!,
                hasSearchBar = false,
                isLeadingIconMenu = false,
                isLeadingIconBackArrow = true
            )
        }
    ) {
        val profilePic = runBlocking { viewModel.getProfilePicOfUser(AppContext.getCurrentUser()) }
        val profilePicState = remember { mutableStateOf(profilePic) }
        val capturedImageUri: MutableState<Uri> = remember { mutableStateOf(Uri.EMPTY) }
        Column(
            modifier = Modifier.fillMaxHeight().padding(paddingValues).padding(vertical = 20.dp, horizontal = 15.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                bitmap = if (profilePicState.value != null) {
                    getBitmap(
                        AppContext.getContext()!!.applicationContext.contentResolver,
                        Uri.parse(profilePicState.value)
                    ).asImageBitmap()
                } else {
                    ImageBitmap.imageResource(id = R.drawable.placeholder)
                },
                contentDescription = AppContext.getContext()!!.getString(R.string.description_profile_picture),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .requiredSize(width = 360.dp, height = 350.dp)
                    .clip(RoundedCornerShape(5.dp))
            )
            TakePhotoButton(
                modifier = Modifier.padding(top = 20.dp).fillMaxWidth(),
                capturedImageUri = capturedImageUri
            ) {
                Text(AppContext.getContext()!!.getString(R.string.btn_photo_select))
            }
        }
        if (capturedImageUri.value.path?.isNotEmpty() == true) {
            saveImage(AppContext.getContext()!!.applicationContext.contentResolver, capturedImageUri.value)
            viewModel.updateProfilePic(AppContext.getCurrentUser(), capturedImageUri.value.toString())
            profilePicState.value = capturedImageUri.value.toString()
        }
    }
}