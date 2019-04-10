package pasu.nakshatraDesigners.data

//"welcomeuser": "Welcome Remya Krishna",
//"currenttime": 1552593073,
//"expirydatetime": "1555179846",
//"imageurl": "https://www.nakshatradesigners.com/aari_image/",
//"videourl": "https://www.nakshatradesigners.com/public/aari_online_classes/",
//"start_title": "How To Start",
data class CertificateListResponse(val status: Int,val webpageurl: ArrayList<NavData>, val message: String, val data: ListDetail) {
    data class ListDetail(
        val timedisplay:String,
        val welcomeuser: String,
        val currenttime: Long=0L,
        val expirydatetime: Long=0L,
        val imageurl: String,
        val videourl: String,
        val start_title: String
        , val basic_title: String,
        val advance_title: String,
        val message: String,
        val start: List<VideoListItem>,
        val basic: List<VideoListItem>,
        val advance: List<VideoListItem>
    )
}