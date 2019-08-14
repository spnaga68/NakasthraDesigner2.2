package pasu.nakshatraDesigners.data

import android.content.Context
import pasu.nakshatraDesigners.utils.IMAGE_URL
import pasu.nakshatraDesigners.utils.Session
import pasu.nakshatraDesigners.utils.VIDEO_URL

data class VideoListItem(
    val aari_name: String, val aari_url: String,
    val aari_video_url: String, val listType: Int = 0
) {

    fun getVideoUrl(context: Context) :String{
return Session.getSession(VIDEO_URL,context )+aari_video_url+".mp4"
    }
    fun getImageUrl(context: Context) :String{
        println("image ${Session.getSession(IMAGE_URL,context )+aari_url+".png"}")
        return Session.getSession(IMAGE_URL,context )+aari_url+".png"
    }

}