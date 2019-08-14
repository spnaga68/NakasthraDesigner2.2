package pasu.nakshatraDesigners.signIn.data

data class ResendOtpResponseData(val status: Int, val message: String, val detail: OtpDetail) {
    data class OtpDetail(val phoneNumber: String, val countryCode: String, val userOtp: String)
}

data class ForgotPasswordResponseData(val status: Int, val message: String,val otp_reference:String,val otp_unique:String, val detail: Detail) {
    data class Detail(val phoneNumber: String, val countryCode: String)
}

data class FacebookSingInResponse(val status: Int, val message: String, val detail: FacebookSignInDetail) {
    data class FacebookSignInDetail(val userId: String, val userEmail: String, val userName: String, val phoneNumber: String, val countryCode: String)
}