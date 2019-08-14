package pasu.nakshatraDesigners.signIn.data.apiData

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PasswordRequestData(val userId: String, val phoneNumber: String, val countryCode: String, val userPassword: String, val deviceId: String, val deviceToken: String, val language: String = "en", val deviceType: Int = 2) : Parcelable