package com.example.final_project_bryants.data

data class CapsuleItems(
    @PrimaryKeey(autoGenerate = true) val itemId: Int = 0,
    val capsuleId: Int,
    val contentType: String, // this is the text
    val content: String,
    val timeStamp: Long
)
