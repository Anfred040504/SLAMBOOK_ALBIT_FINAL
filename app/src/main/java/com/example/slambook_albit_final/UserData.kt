package com.example.slambook_albit_final

import java.io.Serializable

data class UserData(
    var nickName: String = "",
    var fullName: String = "",
    var gender: String = "",
    var email: String = "",
    var contact: String = "",
    var address: String = "",
    var birthMonth: String = "",
    var birthDay: String = "",
    var birthYear: String = "",
    var status: String = ""
): Serializable

