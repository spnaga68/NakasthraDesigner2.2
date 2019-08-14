package pasu.nakshatraDesigners.fragments

import android.os.Bundle
import android.os.Handler
import pasu.nakshatraDesigners.WebViewFrag
import pasu.nakshatraDesigners.utils.WEB_LOAD_URL

class CertificateDetailFrag : WebViewFrag() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getString(WEB_LOAD_URL)?.let {
            Handler().postDelayed(Runnable {
                loadUrl(it)
            },500)
        }
    }
}