package pasu.nakshatraDesigners.signIn.data

import androidx.annotation.Nullable

data class SignUpDetail(
    var userid: String, val phone: String? = "",
    val countryCode: String = "",
    val userOtp: String = "",
    val imageUrl: String = "",
    val email: String,
    val name: String,
    val isNewUser: Int = 0,
    val accesskeyval: String = "",
    val address: String = "",
    val city: String = "",
    val state: String = "",
    val zip: String = "",
    val password: String = ""
)