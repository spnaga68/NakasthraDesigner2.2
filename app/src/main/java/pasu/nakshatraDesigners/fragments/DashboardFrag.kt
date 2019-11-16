package pasu.nakshatraDesigners.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.view.setPadding
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.ViewSkeletonScreen
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.gson.Gson
import pasu.nakshatraDesigners.MainActivity
import pasu.nakshatraDesigners.R
import pasu.nakshatraDesigners.adapter.VideoBaseAdapter
import pasu.nakshatraDesigners.adapter.bindImageFromUrl
import pasu.nakshatraDesigners.data.*
import pasu.nakshatraDesigners.signIn.SignInActivity
import pasu.nakshatraDesigners.utils.DisplayUtils
import pasu.nakshatraDesigners.utils.Session
import pasu.nakshatraDesigners.utils.USER_NAME
import pasu.nakshatraDesigners.utils.widgets.CustomTextview
import pasu.nakshatraDesigners.viewModel.DashboardViewModel
import pasu.nakshatraDesigners.viewModel.ViewmodelFactory
import java.util.*
import kotlin.collections.ArrayList


class DashboardFrag : Fragment() {
    private lateinit var adapter: VideoBaseAdapter
    private lateinit var skeletonBanner: ViewSkeletonScreen
    private lateinit var skeletonInfo: ViewSkeletonScreen
    private lateinit var skeletonList: RecyclerViewSkeletonScreen
    private lateinit var itemListViewModel: DashboardViewModel
    private lateinit var binding: pasu.nakshatraDesigners.databinding.FragDashBinding
    private var reviewArraySize = 0
    //  private var onlineClassesList: ArrayList<OnlineClasses>? = null

    companion object {
        var onlineClassesList: ArrayList<OnlineClasses>? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.frag_dash, container, false)

        //getting our ItemViewModel
        itemListViewModel = ViewModelProviders.of(this, ViewmodelFactory(context!!))
            .get(DashboardViewModel::class.java)

        binding.apply {
            setLifecycleOwner( this@DashboardFrag)
            executePendingBindings()
        }
//        setSkimmerViews()
        isStoragePermissionGranted()


        itemListViewModel.getDashboardData()


        /* binding.ivOne.setOnClickListener {
             it.getTag()?.let {
                 val intent = Intent(context, VideoListActivity::class.java)
                 intent.apply {
                     putExtra(VIDEO_URL, it.toString())
                     startActivity(intent)
                 }
             }
         }
         binding.ivTwo.setOnClickListener {
             it.getTag()?.let {
                 val intent = Intent(context, VideoListActivity::class.java)
                 intent.apply {
                     putExtra(VIDEO_URL, it.toString())
                     startActivity(intent)
                 }
             }
         }*/

        itemListViewModel.dashboardResponse.observe(this, Observer {
            if (it != null) {
                it.data?.let { data ->
                    data.review?.let {
                        setImageSlider(it)
                        reviewArraySize = it.size

                    }
                    addLearn(data.points ?: ArrayList<String>())
                    binding.rootScrollView.scrollTo(0, 0)
                    binding.layoutLoading.visibility = View.GONE
                    if (data.banner.isNotEmpty())
                        addBannerView(data.banner)
                    if (data.video.isNotEmpty())
                        addVideosView(data.video)
                    onlineClassesList = data.onlineclasses
                    print("onlineSelection@ ${Gson().toJson(onlineClassesList)}")
                    Session.save("onlineSelection", Gson().toJson(onlineClassesList), context)

                }
            }
        })

        if (!Session.getUserID(context!!).equals("")) {
            binding.root.findViewById<TextView>(R.id.tvTitle)
                .setText("WELCOME " + Session.getSession(USER_NAME, context!!))
        }


        return binding.root
    }

    private fun hideSkimmerViews() {
        skeletonBanner.hide()
        skeletonInfo.hide()
//        skeletonList.hide()
    }

    private fun setSkimmerViews() {
        skeletonBanner = Skeleton.bind(binding.bannerList)
            .load(R.layout.skeleton_banner_view)
            .show()
        skeletonInfo = Skeleton.bind(binding.infoList)
            .load(R.layout.skeleton_info_view)
            .show()
//        skeletonList = Skeleton.bind(binding.rvList)
////            .adapter(adapter)
//            .load(R.layout.skeleton_sm_list)
//            .show()
    }

    private fun addBannerView(bannerListData: ArrayList<BannerImageData>) {
        binding.bannerList.removeAllViews()
        val layoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        layoutParams.setMargins(0, 5, 20, 5)
        val imgLayoutParams = LinearLayout.LayoutParams(
            DisplayUtils.getWidth(activity!!) - DisplayUtils.dpToPxInt(50),
            500
        )
//        imgLayoutParams.setMargins(0, 0, 10, 0)
        val linearLayout = LinearLayout(activity)
        linearLayout.layoutParams = layoutParams
        linearLayout.orientation = LinearLayout.HORIZONTAL
        for (i in 0 until bannerListData.size) {
            val ivItem = ImageView(activity)
            ivItem.id = i
            ivItem.layoutParams = imgLayoutParams
            ivItem.scaleType = ImageView.ScaleType.FIT_XY
            bindImageFromUrl(ivItem, bannerListData[i].bannerimg)
            val card = CardView(requireContext()).apply {
                this.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(0, 5, 10, 5) }
                radius = 9f
                setContentPadding(0, 0, 0, 0)
                setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                maxCardElevation = 0f
                cardElevation = 0f
                addView(ivItem)
            }
            linearLayout.addView(card)
        }
        binding.bannerList.addView(linearLayout)
    }

    private fun addVideosView(videoListData: ArrayList<BannerVideoData>) {

        var layoutManager = GridLayoutManager(activity, 2)
//        layoutManager.isAutoMeasureEnabled = true;
//        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//            override fun getSpanSize(position: Int): Int {
//                when (adapter.getItemViewType(position)) {
//
//
//                    R.layout.certificate_list_item -> return 2
//
//                    else -> return 2
//                }
//            }
//        }


        binding.viedosList!!.layoutManager = layoutManager
        binding.viedosList!!.setHasFixedSize(true)


        var videolist = ArrayList<VideoListItem>()
        adapter = VideoBaseAdapter(context!!, videolist)
        for (video in videoListData) {
            videolist.add(VideoListItem(video.name, video.imageurl, video.videourl, 0))
        }
        adapter = VideoBaseAdapter(context!!, videolist)
        binding.viedosList.adapter = adapter

//        binding.viedosList.removeAllViews()
//        val mLayoutParams = LinearLayout.LayoutParams(
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//        mLayoutParams.setMargins(0, 5, 20, 5)
//        val imgLayoutParams = LinearLayout.LayoutParams(
//            (DisplayUtils.getWidth(requireActivity()) - DisplayUtils.dpToPxInt(70)) / 2,
//            400
//        )
//        val linearLayout = LinearLayout(requireContext())
//        linearLayout.layoutParams = mLayoutParams
//        linearLayout.orientation = LinearLayout.HORIZONTAL
//        for (i in 0 until bannerListData.size) {
//            val card = CardView(requireContext()).apply {
//                this.layoutParams = LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//                ).apply { setMargins(0, 5, 10, 5) }
//                radius = 9f
//                setContentPadding(0, 0, 0, 0)
//                setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
//                maxCardElevation = 0f
//                cardElevation = 0f
//                addView(LinearLayout(requireContext()).apply {
//                    orientation = LinearLayout.VERTICAL
//                    layoutParams = LinearLayout.LayoutParams(
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT
//                    )
//                    addView(ImageView(requireContext()).apply {
//                        id = i
//                        tag = bannerListData[i].videourl
//                        layoutParams = imgLayoutParams
//                        scaleType = ImageView.ScaleType.FIT_XY
//                        bindImageFromUrl(this, bannerListData[i].imageurl)
//                        setOnClickListener {
//                            it.tag?.let {
//                                val intent = Intent(context, VideoListActivity::class.java)
//                                intent.apply {
//                                    putExtra(VIDEO_URL, it.toString())
//                                    startActivity(intent)
//                                }
//                            }
//                        }
//                    })
//
//
//                    addView(CustomTextview(requireContext()).apply {
//                        id = i
//                        tag = bannerListData[i].videourl
//                        layoutParams = LinearLayout.LayoutParams(
//                            (DisplayUtils.getWidth(requireActivity()) - DisplayUtils.dpToPxInt(70)) / 2,
//                            ViewGroup.LayoutParams.WRAP_CONTENT
//                        )
//                        text = bannerListData[i].name
//                        setTextColor(Color.BLACK)
//                        setType(getString(R.string.style_sub_text))
//                        gravity = Gravity.BOTTOM or Gravity.CENTER
//                    })
//
//                })
//
//
//            }
//            linearLayout.addView(card)
//        }
//        binding.viedosList.addView(linearLayout)
    }

    override fun onResume() {
        super.onResume()

        val mAdView = binding.root.findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        val mAdView2 = binding.root.findViewById<AdView>(R.id.adView2)
        val adRequest2 = AdRequest.Builder().build()
        mAdView2.loadAd(adRequest2)

        with(requireActivity() as MainActivity) {
            onlineclasses.setOnClickListener {
                if (Session.getUserID(this).isEmpty()) {
                    this.startActivity(Intent(this, SignInActivity::class.java))
                } else {
                    onlineClassesList?.let { it1 ->
                        supportFragmentManager.beginTransaction().add(
                            R.id.nav_host_frag,
                            SelectCoursesFragment.newInstance()
                            , Gson().toJson(
                                NavData(getString(R.string.online_classess), 76, "")
                            )
                        ).addToBackStack(null).commit()
                    }
                }
            }
        }
    }


    private fun addLearn(data: ArrayList<String>) {
        binding.points.removeAllViews()
        val layoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        layoutParams.setMargins(4, 8, 4, 8)
        for (i in 0 until data.size) {
            val ivItem = CustomTextview(context!!)
            ivItem.id = i
            ivItem.text = data[i]
            ivItem.setPadding(10)
            ivItem.textSize = 16f
            ivItem.compoundDrawablePadding = 10
            ivItem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_black_24dp, 0, 0, 0);
            binding.points.addView(ivItem)

        }

    }


    fun isStoragePermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(context!!, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.v("", "Permission is granted")
                return true
            } else {
                Log.v("", "Permission is revoked")
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                );
                return false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("", "Permission is granted")
            return true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        if (view != null) {
//            val parentViewGroup = view!!.parent as ViewGroup
//            parentViewGroup.removeAllViews()
//        }
    }


    private fun setImageSlider(review: ArrayList<Review>) {
        if (review != null) {
            binding.imageViewPager.adapter = SliderAdapter(requireContext(), review)
            val timer = Timer()
            timer.scheduleAtFixedRate(SliderTimer(), 3000, 6000)
        }
    }


    inner class SliderAdapter(
        private val context: Context,
        private val imageArray: ArrayList<Review>
    ) :
        PagerAdapter() {

        override fun getCount(): Int {
            return imageArray.size
        }

        override fun isViewFromObject(view: View, anyObject: Any): Boolean {
            return view === anyObject
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.item_image_slider, container, false)
            val cusName = view.findViewById<AppCompatTextView>(R.id.customerName)
            val cusRating = view.findViewById<RatingBar>(R.id.ratingBar)
            val cusReview = view.findViewById<AppCompatTextView>(R.id.reviewText)
            cusName.text = imageArray[position].name + "-" + imageArray[position].city
            cusReview.text = imageArray[position].description
            cusRating.rating = imageArray[position].star?.toFloat() ?: 0f

            val viewPager = container as ViewPager
            viewPager.addView(view, 0)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, anyObject: Any) {
            val viewPager = container as ViewPager
            val view = anyObject as View
            viewPager.removeView(view)
        }
    }

    private inner class SliderTimer : TimerTask() {
        override fun run() {
            activity?.runOnUiThread(Runnable {
                if (binding.imageViewPager.currentItem < reviewArraySize - 1) {
                    binding.imageViewPager.currentItem = binding.imageViewPager.currentItem + 1
                } else {
                    binding.imageViewPager.currentItem = 0
                }
            })
        }
    }


}