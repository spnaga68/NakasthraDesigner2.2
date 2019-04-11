package pasu.nakshatraDesigners.utils.widgets

import android.content.Context
import android.util.AttributeSet
import pasu.nakshatraDesigners.R
import android.graphics.Typeface
import androidx.appcompat.widget.AppCompatTextView
import pasu.nakshatraDesigners.utils.widgets.CustomTextview
import androidx.core.content.ContextCompat


class CustomTextview @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0,
        defStyleRes: Int = 0) : AppCompatTextView(context, attrs, defStyle) {

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it,
                    R.styleable.CustomTextview, defStyle, 0)
            val type = typedArray.getString(R.styleable.CustomTextview_textview_type)

            var typeface: Typeface? = null


            when (type) {
                context.getString(R.string.style_text_header) -> {
                    typeface = Typeface.createFromAsset(context.getAssets(),
                            context.getString(R.string.header_text_font))
                    setTextColor(ContextCompat.getColor(context, R.color.black))
                    println("text default" + text)
                }
                context.getString(R.string.style_sub_text) -> {
                    typeface = Typeface.createFromAsset(context.getAssets(),
                            context.getString(R.string.sub_text_font))
                    setTextColor(ContextCompat.getColor(context, R.color.black))
                    println("text default" + text)
                }

            }
            if (typeface == null)
                typeface = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.normal_text_font))


            setTypeface(typeface)
            typedArray.recycle()
        }
    }

}