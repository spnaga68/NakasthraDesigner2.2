package pasu.nakshatraDesigners.signIn.data.apiData

import pasu.nakshatraDesigners.signIn.data.SignUpDetail

data class SignUpResponseData(val responseCode: Int, val Message: String,  val data: SignUpDetail)