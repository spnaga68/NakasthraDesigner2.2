package pasu.nakshatraDesigners.utils

import java.util.concurrent.TimeUnit
import javax.xml.datatype.DatatypeConstants.SECONDS



class TimeUtils {
    companion object {
        fun convertFromDuration(timeInSeconds: Long): TimeInHours {
            var time = timeInSeconds


            val day = TimeUnit.SECONDS.toDays(time).toInt()
            val hours = TimeUnit.SECONDS.toHours(time) - day * 24
            val minutes = TimeUnit.SECONDS.toMinutes(time) - TimeUnit.SECONDS.toHours(time) * 60
            val seconds = TimeUnit.SECONDS.toSeconds(time) - TimeUnit.SECONDS.toMinutes(time) * 60

////            val days=
//            val hours = time / 3600
//            time %= 3600
//            val minutes = time / 60
//            time %= 60
//            val seconds = time
//

            return TimeInHours(day,hours.toInt(), minutes.toInt(), seconds.toInt())
        }

        class TimeInHours(val days:Int,val hours: Int, val minutes: Int, val seconds: Int) {
            override fun toString(): String {
                return String.format("%dh : %02dm : %02ds", hours, minutes, seconds)
            }
        }
    }
}