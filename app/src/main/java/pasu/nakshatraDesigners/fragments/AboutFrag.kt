package pasu.nakshatraDesigners.fragments

import android.os.Bundle
import android.os.Handler
import pasu.nakshatraDesigners.WebViewFrag
import pasu.nakshatraDesigners.utils.ABOUT_US
import pasu.nakshatraDesigners.utils.Session

class AboutFrag : WebViewFrag() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
Handler().postDelayed(Runnable {
    loadUrl(Session.getSession(ABOUT_US,context))
},500)

    }
}