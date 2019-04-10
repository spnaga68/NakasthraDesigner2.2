package pasu.nakshatraDesigners.data

data class CertificateListRequest(val userid:String, val accesskey:String, val countPerPage: Int, val pageNumber: String) {
}