package pasu.nakshatraDesigners.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import pasu.nakshatraDesigners.R
import pasu.nakshatraDesigners.data.NavData
import pasu.nakshatraDesigners.fragments.CertificateDetailFrag
import pasu.nakshatraDesigners.fragments.Certificates
import pasu.nakshatraDesigners.fragments.ContactUsFragment
import pasu.nakshatraDesigners.fragments.SelectCoursesFragment
import pasu.nakshatraDesigners.rvItemClick
import pasu.nakshatraDesigners.signIn.SignInActivity
import pasu.nakshatraDesigners.utils.Session
import pasu.nakshatraDesigners.utils.WEB_LOAD_URL
import pasu.nakshatraDesigners.utils.widgets.CustomTextview


class NavigataionAdapter(val context: Context, val myDataSet: ArrayList<NavData>, val clickListener: rvItemClick?) :
    RecyclerView.Adapter<NavigataionAdapter.MyViewHolder>() {
    // Create new views (invoked by the layout manager)

    var selectedItem = "-6"


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(pasu.nakshatraDesigners.R.layout.simple_textview, parent, false)

        return MyViewHolder(itemView as CustomTextview)
    }

//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): NavigataionAdapter.MyViewHolder {
//        // create a new view
//        val textView = CustomTextview(context)
//        textView.layoutParams =
//            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        textView.setPadding(DisplayUtils.dpToPxInt(10))
//        textView.textSize = 16f
//        textView.compoundDrawablePadding = 10
//        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_black_24dp, 0, 0, 0);
//
//        // set the view's size, margins, paddings and layout parameters
//        return NavigataionAdapter.MyViewHolder(textView)
//    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: NavigataionAdapter.MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setOnClickListener {
            if(clickListener!=null)
            clickListener.itemClicked(position,myDataSet[position])


            itemClicked(position,myDataSet[position])

            selectedItem = ""+myDataSet[position].id
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

    class MyViewHolder(val textView: CustomTextview) : RecyclerView.ViewHolder(textView)








     fun itemClicked(position: Int, data: NavData) {
        if (data.id == 1) {
            if (Session.getUserID(context!!).equals("")) {
                context.startActivity(Intent(context!!, SignInActivity::class.java))
            } else {
                (context as AppCompatActivity).supportFragmentManager.beginTransaction().add(
                    R.id.nav_host_frag,
                    SelectCoursesFragment.newInstance()
                    , Gson().toJson(
                        NavData(context.getString(R.string.online_classess), 76, "")
                    )
                ).addToBackStack(null).commit()
//                (context as AppCompatActivity).supportFragmentManager.beginTransaction().add(
//                    R.id.nav_host_frag,
//                    Certificates()
//                    ,  Gson().toJson(data)
//                ).addToBackStack(null).commit()
            }
        } else if (data.id == -6) {

        } else if (data.id == 1000) {
            println("this is data $data")
            (context as AppCompatActivity).supportFragmentManager.beginTransaction().add(
                R.id.nav_host_frag,
                ContactUsFragment()
                , Gson().toJson(data)
            ).addToBackStack(null).commit()
        } else {
            var fragment = CertificateDetailFrag()
            var bundle = Bundle()
            bundle.putString(WEB_LOAD_URL, data.url)
            fragment.arguments = bundle
            (context as AppCompatActivity).supportFragmentManager.beginTransaction().add(
                R.id.nav_host_frag,
                fragment
                , "" +Gson().toJson(data)
            ).addToBackStack(null).commit()
//            onBackPressed()
        }
    }







}