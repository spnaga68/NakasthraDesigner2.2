package pasu.nakshatraDesigners.utils.widgets

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import pasu.nakshatraDesigners.utils.DisplayUtils
import android.widget.GridView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import nakshatraDesigners.utils.CommonFunctions
import pasu.nakshatraDesigners.R
import pasu.nakshatraDesigners.utils.Session


class SocialLinkView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle) {
    init {
        var v=LayoutInflater.from(context).inflate(R.layout.social_lay,this,false)
        addView(v)
        orientation= VERTICAL
        var gridView= v.findViewById<RecyclerView>(R.id.rv_list)
        gridView.layoutManager=GridLayoutManager(context,3)
//        addView(gridView)

        val adapter = GridSocialAdapter(context,Session.getSocialLink(context))
        gridView.adapter = adapter



        v.findViewById<Button>(R.id.callButton).setOnClickListener {
            CommonFunctions.callClicked(
                context!!,
                Session.getSplashDATA(context!!).phone
            )
        }
        v.findViewById<Button>(R.id.whatsappButton).setOnClickListener {
            CommonFunctions.onClickWhatsApp(context!!, Session.getSplashDATA(context!!).whatsapp)
        }

    }

    override fun setEnabled(enabled: Boolean) {
        setAlpha(if (enabled) 1f else 0.2f)
        super.setEnabled(enabled)
    }
}