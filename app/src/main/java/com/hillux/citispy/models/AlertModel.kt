package com.hillux.citispy.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class AlertModel(
    var location: String = "",
    var time: String = "",
    var type: String = ""
) {
}