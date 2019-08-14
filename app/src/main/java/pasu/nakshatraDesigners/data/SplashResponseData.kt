package pasu.nakshatraDesigners.data

data class SplashResponseData(val responseCode: Int, val Message: String, val data: SplashData) {
    data class SplashData(
        val Detaildata: CoreData?,
        val socialmedialink: ArrayList<SocialMediaData>, val webpageurl: ArrayList<NavData>,
        val phone: String,
        val whatsapp: String,
        val email: String,
        val address1: String,
        val address1_longitude: String,
        val address1_latitude: String,
        val address2: String, val address2_longitude: String,
        val address2_latitude: String
        )


//    "phone": "9791776417",
//    "whatsapp": "9787272400",
//    "email": "nakshatra.keerthana@gmail.com",
//    "address1": "No.114, First Floor Harshini illam, IV Cross street, Sengaliappa Nagar,V.K.Road, Peelamedu, Coimbatore - 641004",
//    "address1_longitude": "77.677597",
//    "address1_latitude": "11.320270",
//    "address2": "No.115, Perundurai Main Road, Near Panchayat Office, Thindal, Erode - 638009.",
//    "address2_longitude": "77.008844",
//    "address2_latitude": "11.032592",
}


