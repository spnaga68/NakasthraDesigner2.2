package pasu.nakshatraDesigners

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import kotlinx.android.synthetic.main.activity_webview.*


open class WebViewFrag : Fragment() {
    lateinit var simpleWebView: WebView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.activity_webview, container, false)
        simpleWebView = v.findViewById(R.id.wvLocalhtml)
        simpleWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                layoutLoading?.visibility = View.GONE
            }
        }
        val mAdView = v.findViewById<AdView>(R.id.adView2)
        val adRequest2 = AdRequest.Builder().build()
        mAdView.loadAd(adRequest2)


        return v;
    }


    fun loadUrl(url:String){
        simpleWebView.loadUrl(url)
    }


//
}