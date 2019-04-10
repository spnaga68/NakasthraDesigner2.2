package pasu.nakshatraDesigners.data


data class DashboardResponse(
    var activeCerificate: Int?,
    var activeVehicles: Int?,
    var status: Int,
    var message: String
){
    fun getActiveCertificatesText():String {
        return activeCerificate?.toString() ?: "-"
    }
    fun getactiveVehicleText():String{
        return activeVehicles?.toString() ?: "-"
    }
}
