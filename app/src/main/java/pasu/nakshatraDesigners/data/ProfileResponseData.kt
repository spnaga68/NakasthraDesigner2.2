package pasu.nakshatraDesigners.data

import pasu.nakshatraDesigners.signIn.data.SignUpDetail

data class ProfileResponseData(val responseCode:Int, val Message:String, var data : ArrayList<SignUpDetail>)  {
    data class ProfileData(val name:String, val lName: String, val email:String, val mobile:String, var imageUrl:String, val userid:String, var image_url:String="")
}