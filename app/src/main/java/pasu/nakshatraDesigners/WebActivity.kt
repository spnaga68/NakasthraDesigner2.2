package pasu.nakshatraDesigners

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.include_header.*
import pasu.nakshatraDesigners.utils.WEB_LOAD_URL

class WebActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        intent?.getStringExtra(WEB_LOAD_URL)?.let {
        }
        titleText.visibility= View.VISIBLE
        toolbarImage.visibility=View.GONE
        titleText.text = intent.getStringExtra("title")
        tool_back.visibility=View.VISIBLE
        tool_back.setOnClickListener { finish() }
    }
}