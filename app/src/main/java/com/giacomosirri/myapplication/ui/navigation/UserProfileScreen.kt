package com.giacomosirri.myapplication.ui.navigation

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.giacomosirri.myapplication.R
import com.giacomosirri.myapplication.ui.AppContext
import com.giacomosirri.myapplication.viewmodel.AppViewModel
import kotlinx.coroutines.runBlocking
import java.util.*

@Composable
fun UserProfileScreen(
    paddingValues: PaddingValues,
    username: String,
    onWishlistButtonClick: () -> Unit,
    viewModel: AppViewModel,
    navController: NavController
) {
    val openDialog = remember { mutableStateOf(false) }
    val relationshipTypes = listOf(
        AppContext.getContext()!!.getString(R.string.relationship_type_friend),
        AppContext.getContext()!!.getString(R.string.relationship_type_family),
        AppContext.getContext()!!.getString(R.string.relationship_type_partner),
        AppContext.getContext()!!.getString(R.string.relationship_type_colleague),
        AppContext.getContext()!!.getString(R.string.relationship_type_none))
    val (selected, onSelected) = remember { mutableStateOf(relationshipTypes[0]) }
    val commonEvents = viewModel.getCommonEvents(username).collectAsState(initial = emptyList())
    var subscriptionDate: Date?
    var profilePic: String?
    runBlocking {
        subscriptionDate = viewModel.getSubscriptionDate(username)
        profilePic = viewModel.getProfilePicOfUser(username)
    }
    Scaffold(
        topBar = {
            NavigationAppBar(
                currentScreenName = username,
                hasSearchBar = false,
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 4.dp, bottom = 20.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Wishlist button
            Button(
                onClick = { onWishlistButtonClick() },
                modifier = Modifier
                    .padding(top = 10.dp),
                shape = RoundedCornerShape(5.dp)
            ) {
                Text(text = AppContext.getContext()!!.getString(R.string.btn_view_wishlist))
            }
            // Profile pic
            if (profilePic != null) {
                AsyncImage(
                    model = ImageRequest.Builder(AppContext.getContext()!!).data(Uri.parse(profilePic)).crossfade(true).build(),
                    contentDescription = AppContext.getContext()!!.getString(R.string.description_profile_picture),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .requiredSize(width = 350.dp, height = 350.dp)
                        .clip(RoundedCornerShape(5.dp))
                )
            } else {
                Image(
                    bitmap = ImageBitmap.imageResource(id = R.drawable.placeholder),
                    contentDescription = AppContext.getContext()!!.getString(R.string.description_profile_picture),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .requiredSize(width = 350.dp, height = 350.dp)
                        .clip(RoundedCornerShape(5.dp))
                )
            }
            // Registration date
            Text(
                text = AppContext.getContext()!!.getString(R.string.label_registered_since) + " ${subscriptionDate?.let { profileDateFormat.format(it) }}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            // Relationship status
            Row(
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = AppContext.getContext()!!.getString(R.string.label_select_relationship_status),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                OutlinedButton(onClick = { openDialog.value = true }) {
                    Text(text = selected)
                }
            }
            // Upcoming events
            if (commonEvents.value.isNotEmpty()) {
                Column {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = AppContext.getContext()!!
                                .getString(R.string.label_common_upcoming_events),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    LazyColumn(modifier = Modifier.padding(10.dp)) {
                        for (event in commonEvents.value) {
                            item {
                                EventCard(event, navController, viewModel)
                            }
                        }
                    }
                }
            }
        }
        if (openDialog.value) {
            SingleChoiceDialog(title = AppContext.getContext()!!.getString(R.string.dialog_relationship_select_title), relationshipTypes, selected, onSelected, openDialog) {
                viewModel.updateRelationship(AppContext.getCurrentUser(), username, selected)
            }
        }
    }
}