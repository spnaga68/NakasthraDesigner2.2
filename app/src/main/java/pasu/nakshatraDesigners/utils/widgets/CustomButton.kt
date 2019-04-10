package pasu.nakshatraDesigners.utils.widgets

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import pasu.nakshatraDesigners.R
import pasu.nakshatraDesigners.utils.DisplayUtils.Companion.dpToPxInt

class CustomButton @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0,
        defStyleRes: Int = 0
) : AppCompatButton(context, attrs, defStyle) {
    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it,
                    R.styleable.CustomButton, defStyle, 0)
            val type = typedArray.getString(R.styleable.CustomButton_type)

             var typeface: Typeface?=null


            when (type) {
                context.getString(R.string.postive_button) -> {
                    typeface = Typeface.createFromAsset(context.getAssets(),
                            context.getString(R.string.normal_text_font))
                    setPadding(dpToPxInt(10), dpToPxInt(15), dpToPxInt(15), dpToPxInt(15))
                    setBackgroundResource(R.drawable.buttonbg)
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    println("text default" + text)
                }

                context.getString(R.string.negative_button) -> {
                    typeface = Typeface.createFromAsset(context.getAssets(),
                        context.getString(R.string.normal_text_font))
                    setPadding(dpToPxInt(10), dpToPxInt(15), dpToPxInt(15), dpToPxInt(15))
                    setBackgroundResource(R.drawable.neg_buttton_bg)
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    println("text default" + text)
                }

            }
            if (typeface == null)
                typeface = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.normal_text_font))


            setTypeface(typeface)
            typedArray.recycle()
        }
    }

    override fun setEnabled(enabled: Boolean) {
        setAlpha(if (enabled) 1f else 0.2f)
        super.setEnabled(enabled)
    }
}