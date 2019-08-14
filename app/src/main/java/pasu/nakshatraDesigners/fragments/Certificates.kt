package pasu.nakshatraDesigners.fragments

import android.graphics.Paint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.frag_certifcate_list.*
import pasu.nakshatraDesigners.R
import pasu.nakshatraDesigners.adapter.CertificateAdapter
import pasu.nakshatraDesigners.data.CertificateListResponse
import pasu.nakshatraDesigners.data.NavData
import pasu.nakshatraDesigners.data.VideoListItem
import pasu.nakshatraDesigners.utils.EXPIRY_TIME
import pasu.nakshatraDesigners.utils.Session
import pasu.nakshatraDesigners.utils.TimeUtils.Companion.convertFromDuration
import pasu.nakshatraDesigners.utils.USER_NAME
import pasu.nakshatraDesigners.utils.widgets.CustomTextview
import pasu.nakshatraDesigners.viewModel.CertificateListViewModel
import pasu.nakshatraDesigners.viewModel.ViewmodelFactory

private const val ARG_PARAM1 = "courseUrlToLoad"

class Certificates : Fragment() {
    private lateinit var adapter: CertificateAdapter
    private lateinit var itemViewModel: CertificateListViewModel
    //    private lateinit var skeletonScreen: RecyclerViewSkeletonScreen
    private var recyclerView: RecyclerView? = null
    private lateinit var dataObserver: Observer<PagedList<VideoListItem>>
    private var courseUrl: String? = null

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param courseUrl Parameter 1.
         * @return A new instance of fragment SelectCoursesFragment.
         */
        @JvmStatic
        fun newInstance(courseUrl: String) =
            Certificates().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, courseUrl)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            courseUrl = getString(ARG_PARAM1) ?: "aarionlineclasses.html"
        }
        if (courseUrl.isNullOrEmpty())
            courseUrl = "aarionlineclasses.html"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.frag_certifcate_list, container, false)
        // This method will be called when a MessageEvent is posted (in the UI thread for Toast)


        //setting up recyclerview1
        recyclerView = v.findViewById(R.id.rv_list)
        v.findViewById<TextView>(R.id.tvTitle).text =
            "WELCOME " + Session.getSession(USER_NAME, context!!)
        var layoutManager = GridLayoutManager(activity, 2)
//        layoutManager.isAutoMeasureEnabled = true;
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                when (adapter.getItemViewType(position)) {


                    R.layout.certificate_list_item -> return 1

                    else -> return 2
                }
            }
        }


        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.setHasFixedSize(true)
        adapter = CertificateAdapter(context!!)


//        //setting up recyclerview2
//      var  recyclerView2 = v.findViewById<RecyclerView>(R.id.rv_list2)
//        var layoutManager2 = GridLayoutManager(activity, 2)
////        layoutManager2.isAutoMeasureEnabled = true;
//        var adapter2 =  NavigataionAdapter(context!!, Session.getNavData(context!!), null)
//        layoutManager2.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//            override fun getSpanSize(position: Int): Int {
//                when (adapter2.getItemViewType(position)) {
//
//
//                    R.layout.simple_textview -> return 1
//
//                    else -> return 2
//                }
//            }
//        }
//
//
//        recyclerView2!!.layoutManager = layoutManager2
//        recyclerView2!!.setHasFixedSize(true)
//
//
//        recyclerView2.adapter = adapter2
//
//
//
//


        val expText = v.findViewById<CustomTextview>(R.id.validDate)
        var expTime = 0L
        val ct = object : CountDownTimer(300000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                //((socialmedialink?.socialmedialink?.expirydatetime ?:0)-(socialmedialink?.socialmedialink?.currenttime ?:0))

                expText.text = convertFromDuration(expTime).toString()
                expTime = expTime - 1


                //here you can have your logic to set text to edittext
            }

            override fun onFinish() {
//                mTextField.setText("done!")
            }

        }

        val contactUs = v.findViewById<TextView>(R.id.contact_us_link)
        contactUs.paintFlags = contactUs.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        val factory = ViewmodelFactory(context!!,courseUrl!!)
        //getting our ItemViewModel
        itemViewModel =
            ViewModelProviders.of(this, factory).get(CertificateListViewModel::class.java)
        dataObserver = Observer<PagedList<VideoListItem>> { items ->
            //in case of any changes
            //submitting the items to adapter
//            skeletonScreen?.hide()
            println("Detaildata observed   $items")

            if (Session.getSession("ses_showExp", context!!).equals("1")) {
                validDate.visibility = View.VISIBLE
                validDateLabel.visibility = View.VISIBLE
            } else {
                validDate.visibility = View.GONE
                validDateLabel.visibility = View.GONE
            }
            if (!Session.getSession("ses_video_401", context!!).equals("")) {
                v.findViewById<View>(R.id.error_lay).visibility = View.VISIBLE
                val data = Gson().fromJson(
                    Session.getSession("ses_video_401", context!!),
                    CertificateListResponse.ListDetail::class.java
                )
                v.findViewById<TextView>(R.id.title1).text = data.title
                v.findViewById<TextView>(R.id.title2).text = data.doc1
//                var contactUs = v.findViewById<TextView>(R.id.contactUs)
//                contactUs.setPaintFlags(contactUs.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
                contactUs.text = data.doc2
//                contactUs.setOnClickListener {  println("contact us clicked") }
                contactUs.setOnClickListener {
                    println("contact us clicked")
                    activity?.supportFragmentManager?.beginTransaction()?.replace(
                        R.id.nav_host_frag,
                        ContactUsFragment()
                        , "" + NavData("Contact Us", 1000, "")
                    )?.addToBackStack(null)?.commit()
                }
            } else {
                v.findViewById<View>(R.id.error_lay).visibility = View.GONE
                v.findViewById<View>(R.id.rv_list).visibility = View.VISIBLE
            }


            if (items != null) {
                if (!Session.getSession(EXPIRY_TIME, context).equals("") && Session.getSession(
                        "ses_video_401",
                        context!!
                    ).equals("")
                ) {
                    expTime = Session.getSession(EXPIRY_TIME, context).toLong()
                    ct.start()
                }
                v.findViewById<CustomTextview>(R.id.validDate).text =
                    getString(R.string.valid_upto) + " " + Session.getSession(EXPIRY_TIME, context)
                v.findViewById<ViewGroup>(R.id.layoutLoading).visibility = View.GONE
                adapter.submitList(items)
            } else {
                Toast.makeText(context!!, getString(R.string.server_error), Toast.LENGTH_SHORT)
                    .show()
            }
        }
        //observing the itemPagedList from view model
        itemViewModel.itemPagedList?.observe(this, dataObserver)

        //Observing the networkstate from view model to update the network state
        itemViewModel.networkState?.observe(this, Observer {
            if (it != null) {
                adapter.setNetworkState(it)
            }
        })
        //setting the adapter
        recyclerView!!.adapter = adapter
        itemViewModel.getSearchedData()
        return v
    }

    override fun onResume() {
        super.onResume()
        itemViewModel.itemPagedList?.observe(this, dataObserver)
    }
}