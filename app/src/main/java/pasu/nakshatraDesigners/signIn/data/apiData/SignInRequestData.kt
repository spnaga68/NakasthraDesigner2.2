package pasu.nakshatraDesigners.signIn.data.apiData

data class SignInRequestData(
    var email: String,
    var countryCode: String,
    val password: String,
    val mobiledeviceid: String,
    val deviceToken: String,
    val language: String = "en",
    val deviceType: Int = 2
)