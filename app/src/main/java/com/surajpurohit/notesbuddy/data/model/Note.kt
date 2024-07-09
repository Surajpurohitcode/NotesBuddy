package com.surajpurohit.notesbuddy.data.model

import java.util.Date

data class Note(
    val id: Long = 0,
    val title: String,
    val content: String,
    val date: String
)