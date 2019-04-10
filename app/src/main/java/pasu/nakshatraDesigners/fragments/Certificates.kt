package pasu.nakshatraDesigners.fragments

import android.os.Bundle
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.frag_certifcate_list.*
import pasu.nakshatraDesigners.R
import pasu.nakshatraDesigners.adapter.CertificateAdapter
import pasu.nakshatraDesigners.data.VideoListItem
import pasu.nakshatraDesigners.utils.EXPIRY_TIME
import pasu.nakshatraDesigners.utils.Session
import pasu.nakshatraDesigners.viewModel.CertificateListViewModel
import pasu.nakshatraDesigners.viewModel.ViewmodelFactory
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import android.os.CountDownTimer
import pasu.nakshatraDesigners.utils.TimeUtils.Companion.convertFromDuration


class Certificates : Fragment() {
    private lateinit var adapter: CertificateAdapter;
    private lateinit var itemViewModel: CertificateListViewModel;
    //    private lateinit var skeletonScreen: RecyclerViewSkeletonScreen
    private var recyclerView: RecyclerView? = null


    private lateinit var dataObserver: Observer<PagedList<VideoListItem>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.frag_certifcate_list, container, false)
        // This method will be called when a MessageEvent is posted (in the UI thread for Toast)

        //setting up recyclerview
        recyclerView = v.findViewById(R.id.rv_list)
        var layoutManager = GridLayoutManager(activity, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                when (adapter.getItemViewType(position)) {


                    R.layout.certificate_list_item -> return 1

                    else -> return 2
                }
            }
        }
        var expText = v.findViewById<TextView>(R.id.validDate)
        var expTime = 0L
        var ct = object : CountDownTimer(300000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                //((socialmedialink?.socialmedialink?.expirydatetime ?:0)-(socialmedialink?.socialmedialink?.currenttime ?:0))

                println("expir dataaaa $expTime  ${convertFromDuration(expTime).toString()}")
                expText.text = convertFromDuration(expTime).toString()
                expTime = expTime - 1;


                //here you can have your logic to set text to edittext
            }

            override fun onFinish() {
//                mTextField.setText("done!")
            }

        }





        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.setHasFixedSize(true)
        adapter = CertificateAdapter(context!!)
        val factory = ViewmodelFactory(context!!)
        //getting our ItemViewModel
        itemViewModel = ViewModelProviders.of(this, factory).get(CertificateListViewModel::class.java)
        dataObserver = Observer<PagedList<VideoListItem>> { items ->
            //in case of any changes
            //submitting the items to adapter
//            skeletonScreen?.hide()
            println("Detaildata observed   $items")

            if(Session.getSession("ses_showExp",context!!).equals("1")){
                validDate.visibility=View.VISIBLE
                validDateLabel.visibility=View.VISIBLE
            }else{
                validDate.visibility=View.GONE
                validDateLabel.visibility=View.GONE
            }

            if (items != null) {
                if (!Session.getSession(EXPIRY_TIME, context).equals("")) {
                    expTime = Session.getSession(EXPIRY_TIME, context).toLong()
                    ct.start()
                }
                v.findViewById<TextView>(R.id.validDate).text =
                    getString(R.string.valid_upto) + " " + Session.getSession(EXPIRY_TIME, context)
                v.findViewById<ViewGroup>(R.id.layoutLoading).visibility = View.GONE
                adapter.submitList(items)
            } else {
                Toast.makeText(context!!, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
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


//        skeletonScreen = Skeleton.bind(recyclerView)
//            .adapter(adapter)
//            .load(R.layout.skeleton_sm_list)
//            .show();


        itemViewModel.getSearchedData()
        itemViewModel.itemPagedList?.observe(this, dataObserver)


        return v;


    }

    override fun onResume() {
        super.onResume()
    }


}