package pasu.nakshatraDesigners.signIn.data.apiData

data class SubmitOtpData(val phoneNumber: String, val countryCode: String, val otp: String, val language: String = "en", val deviceType: Int = 2,val otp_unique:String="")