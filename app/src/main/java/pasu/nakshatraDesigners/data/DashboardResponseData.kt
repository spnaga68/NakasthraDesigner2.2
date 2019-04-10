package pasu.nakshatraDesigners.data

data class DashboardResponseData(

    var responseCode: String? = "",
    var message: String? = "",
    var data: Data? = null

)

data class Data(

    var banner1: String? = "",
    var banner2: String? = "",
    var banner3: String? = "",
    var video1: String? = "",
    var video2: String? = "",
    var title: String? = "",
    var video1img: String? = "",
    var video2img: String = "",
    var points: ArrayList<String>,
    var review: ArrayList<Review>? = null

) {
    fun getpoints(): String {
        return ""

    }
}

data class Review(

    var name: String? = null,
    var star: String? = null,
    var description: String? = null,
    var createdon: String? = null,
    var city: String? = null

)