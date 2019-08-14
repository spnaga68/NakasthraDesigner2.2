package pasu.nakshatraDesigners.utils.widgets

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import pasu.nakshatraDesigners.adapter.bindImageFromUrl
import pasu.nakshatraDesigners.data.SocialMediaData
import pasu.nakshatraDesigners.utils.DisplayUtils
import android.content.Intent
import android.net.Uri


//class GridSocialAdapter(val mContext: Context, val socialmedialink: ArrayList<SocialMediaData>) : BaseAdapter() {
//
//    override fun getCount(): Int {
//        return socialmedialink.size
//    }
//
//    // 3
//    override fun getItemId(position: Int): Long {
//        return 0
//    }
//
//    // 4
//    override fun getItem(position: Int): Any? {
//        return socialmedialink[position]
//    }
//
//    // 5
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        println("socialmedialink size ${socialmedialink.size} ${socialmedialink[position].image}")
//
//        var imageView = LayoutInflater.from(mContext).inflate(R.layout.social_lay, parent, false) as AppCompatImageView
////        imageView.layoutParams = ViewGroup.LayoutParams(DisplayUtils.dpToPxInt(50), DisplayUtils.dpToPxInt(50))
//        bindImageFromUrl(imageView,
//            socialmedialink[position].image
//                ?: "https://cdn4.iconfinder.com/data/icons/social-media-icons-the-circle-set/48/facebook_circle-512.png"
//        )
//        imageView.setBackgroundColor(Color.GREEN)
//        return imageView
//    }
//}


class GridSocialAdapter(val context: Context, private val myDataset: ArrayList<SocialMediaData>) :
    RecyclerView.Adapter<GridSocialAdapter.MyViewHolder>() {

    // Provide a reference to the views for each socialmedialink item
    // Complex socialmedialink items may need more than one view per item, and
    // you provide access to all the views for a socialmedialink item in a view holder.
    // Each socialmedialink item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GridSocialAdapter.MyViewHolder {
        // create a new view
        val imageView = ImageView(context)
        imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtils.dpToPxInt(70))
        imageView.setPadding(10)

        // set the view's size, margins, paddings and layout parameters
        return MyViewHolder(imageView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.imageView.setOnClickListener {
            val url = myDataset[position].url
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            context.startActivity(i)
        }
        bindImageFromUrl(
            holder.imageView,
            myDataset[position].image
                ?: "https://cdn4.iconfinder.com/socialmedialink/icons/social-media-icons-the-circle-set/48/facebook_circle-512.png"
        )
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}