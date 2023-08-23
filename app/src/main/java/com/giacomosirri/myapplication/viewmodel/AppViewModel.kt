package com.giacomosirri.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.viewModelScope
import com.giacomosirri.myapplication.repository.EventRepository
import com.giacomosirri.myapplication.repository.ItemRepository
import com.giacomosirri.myapplication.repository.UserRepository

@HiltViewModel
class AppViewModel @Inject constructor(
    eventRepository: EventRepository,
    itemRepository: ItemRepository,
    userRepository: UserRepository
) : ViewModel() {
}