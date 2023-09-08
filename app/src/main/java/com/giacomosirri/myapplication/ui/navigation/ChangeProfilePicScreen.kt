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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
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
        val profilePic = remember { mutableStateOf(runBlocking { viewModel.getProfilePicOfUser(AppContext.getCurrentUser()) }) }
        val capturedImageUri: MutableState<Uri> = remember { mutableStateOf(Uri.EMPTY) }
        Column(
            modifier = Modifier.fillMaxHeight().padding(paddingValues).padding(20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            if (profilePic.value != null) {
                AsyncImage(
                    model = ImageRequest.Builder(AppContext.getContext()!!).data(Uri.parse(profilePic.value)).crossfade(true).build(),
                    contentDescription = AppContext.getContext()!!.getString(R.string.description_profile_picture),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().requiredHeight(350.dp).clip(RoundedCornerShape(5.dp))
                )
            } else {
                Image(
                    bitmap = ImageBitmap.imageResource(id = R.drawable.placeholder),
                    contentDescription = AppContext.getContext()!!.getString(R.string.description_profile_picture),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().requiredHeight(350.dp).clip(RoundedCornerShape(5.dp))
                )
            }
            TakePhotoButton(
                modifier = Modifier.padding(top = 20.dp).fillMaxWidth(),
                capturedImageUri = capturedImageUri
            ) {
                Text(AppContext.getContext()!!.getString(R.string.btn_photo_select))
            }
        }
        if (capturedImageUri.value.path?.isNotEmpty() == true) {
            saveImage(capturedImageUri.value)
            viewModel.updateProfilePic(AppContext.getCurrentUser(), capturedImageUri.value.toString())
            profilePic.value = capturedImageUri.value.toString()
        }
    }
}