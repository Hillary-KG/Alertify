package com.hillux.citispy.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserModel(
    var first_name:String = "",
    var last_name:String = "",
    var phone_number:String = "",
    var userTopic:String = "",
    var userToken: String = "",
    var taggedUsers: List<String> = listOf()
)