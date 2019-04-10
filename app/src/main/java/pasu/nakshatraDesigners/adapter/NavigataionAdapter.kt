package pasu.nakshatraDesigners.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import pasu.nakshatraDesigners.R
import pasu.nakshatraDesigners.data.NavData
import pasu.nakshatraDesigners.fragments.Certificates
import pasu.nakshatraDesigners.rvItemClick
import pasu.nakshatraDesigners.utils.DisplayUtils
import pasu.nakshatraDesigners.utils.Session

class NavigataionAdapter(val context: Context, val myDataSet: ArrayList<NavData>, val clickListener: rvItemClick) :
    RecyclerView.Adapter<NavigataionAdapter.MyViewHolder>() {
    // Create new views (invoked by the layout manager)

    var selectedItem = "-6"

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NavigataionAdapter.MyViewHolder {
        // create a new view
        val textView = TextView(context)
        textView.layoutParams =
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        textView.setPadding(DisplayUtils.dpToPxInt(10))
        textView.textSize = 16f
        textView.compoundDrawablePadding = 10
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_black_24dp, 0, 0, 0);

        // set the view's size, margins, paddings and layout parameters
        return NavigataionAdapter.MyViewHolder(textView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: NavigataionAdapter.MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setOnClickListener {
            clickListener.itemClicked(position,myDataSet[position])
            selectedItem = ""+position
notifyDataSetChanged()

//            val url = myDataSet[position].url
//            val i = Intent(Intent.ACTION_VIEW)
//            i.data = Uri.parse(url)
//            context.startActivity(i)
        }
        holder.textView.text = myDataSet[position].name

        if (selectedItem == ""+myDataSet[position].id)
            holder.textView.setBackgroundColor(ContextCompat.getColor(context, R.color.light_gray))
        else
            holder.textView.setBackgroundColor(ContextCompat.getColor(context, R.color.white))

//        bindImageFromUrl(
//            holder.textView,
//            myDataSet[position].image
//                ?: "https://cdn4.iconfinder.com/data/icons/social-media-icons-the-circle-set/48/facebook_circle-512.png"
//        )
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataSet.size

    class MyViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)
}