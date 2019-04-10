package pasu.nakshatraDesigners.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.util.DisplayMetrics


class DisplayUtils {
    companion object {
        fun dpToPx(dp: Int): Float {
            return (dp * Resources.getSystem().getDisplayMetrics().density)
        }

        fun dpToPxInt(dp: Int): Int {
            return (dp * Resources.getSystem().getDisplayMetrics().density).toInt()
        }

        fun pxToDpInt(px: Int): Int {
            return (px / Resources.getSystem().getDisplayMetrics().density).toInt()
        }


        fun hideKeyboard(context: Context, v: View?) {
            if (v != null) {
                (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    v.windowToken,
                    2
                )
            }
        }

        fun showKeyboard(context: Context, v: View?) {
            if (v != null) {
                (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(v, 1)
                println("sho")
            }
        }

        fun getWidth(context: Activity):Int {
            val displayMetrics = DisplayMetrics()
            context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics)
            return displayMetrics.widthPixels
        }

        fun getHeight(context: Activity) :Int{
            val displayMetrics = DisplayMetrics()
            context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics)
            return displayMetrics.heightPixels
        }

    }
}