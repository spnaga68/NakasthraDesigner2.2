package pasu.nakshatraDesigners.signIn.smsVerifier

interface OnSmsCatchListener<T> {
    fun onSmsCatch(message: String)
}