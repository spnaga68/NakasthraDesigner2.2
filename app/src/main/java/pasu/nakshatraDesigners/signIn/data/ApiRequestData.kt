package pasu.nakshatraDesigners.signIn.data

data class ResendOtpData(val phoneNumber: String, val countryCode: String, val otp: String, val language: String = "en", val deviceType: Int = 2)

data class ForgotPasswordRequest(val phone_no: String, val countryCode: String, val language: String = "en", val deviceType: Int = 2)

data class FacebookSignInRequest(val facebookId: String, val emailId: String, val userName: String, val deviceId: String, val deviceToken: String, val language: String = "en", val deviceType: Int = 2)