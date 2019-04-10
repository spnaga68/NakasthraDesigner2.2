package pasu.nakshatraDesigners.adapter

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import pasu.nakshatraDesigners.R


@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(imageView: ImageView, imageUrl: String?) {


    println("image "+imageUrl)
    if (!imageUrl.isNullOrEmpty()) {


        Picasso.get().load(imageUrl)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .into(imageView, object : Callback {
                override fun onSuccess() {
                    println("nagaPicasso success1")

                }

                override fun onError(e: Exception?) {
                    Log.v("nagaPicasso", "Could not fetch image1 ${e?.localizedMessage}")
                    Picasso.get().load(imageUrl)
                        .into(imageView, object : Callback {
                            override fun onError(e: Exception?) {
                                println("nagaPicasso Could not fetch image2 ${e?.localizedMessage}")
                            }

                            override fun onSuccess() {
                                println("nagaPicasso success2")
                            }

                        })
                }

            })
    }


}
