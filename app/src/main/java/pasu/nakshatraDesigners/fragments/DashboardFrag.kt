package pasu.nakshatraDesigners.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.ViewSkeletonScreen
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import nakshatraDesigners.utils.CommonFunctions
import pasu.nakshatraDesigners.R
import pasu.nakshatraDesigners.VideoListActivity
import pasu.nakshatraDesigners.adapter.bindImageFromUrl
import pasu.nakshatraDesigners.data.Review
import pasu.nakshatraDesigners.utils.DisplayUtils
import pasu.nakshatraDesigners.utils.Session
import pasu.nakshatraDesigners.utils.USER_NAME
import pasu.nakshatraDesigners.utils.VIDEO_URL
import pasu.nakshatraDesigners.utils.widgets.CustomTextview
import pasu.nakshatraDesigners.viewModel.DashboardViewModel
import pasu.nakshatraDesigners.viewModel.ViewmodelFactory
import java.util.*
import kotlin.collections.ArrayList


class DashboardFrag : Fragment() {
    private lateinit var skeletonBanner: ViewSkeletonScreen
    private lateinit var skeletonInfo: ViewSkeletonScreen
    private lateinit var skeletonList: RecyclerViewSkeletonScreen
    private lateinit var itemListViewModel: DashboardViewModel
    private lateinit var binding: pasu.nakshatraDesigners.databinding.FragDashBinding
    private var bannerListData = ArrayList<String>()
    private var reviewArraySize = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.frag_dash, container, false)

        //getting our ItemViewModel
        itemListViewModel = ViewModelProviders.of(this, ViewmodelFactory(context!!)).get(DashboardViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@DashboardFrag
            executePendingBindings()
        }
//        setSkimmerViews()
        itemListViewModel.getDashboardData()


        binding.ivOne.setOnClickListener {
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
        }

        itemListViewModel.dashboardResponse.observe(this, Observer {
            if (it != null) {
                var bannerArray = listOf<String>(it.data?.banner1!!, it.data?.banner2!!, it.data?.banner3!!)
                bannerListData = ArrayList(bannerArray)
                binding.ivOne.setTag(it?.data?.video1)
                binding.ivTwo.setTag(it?.data?.video2)

                bindImageFromUrl(binding.ivOne, it?.data?.video1img)
                bindImageFromUrl(binding.ivTwo, it?.data?.video2img)
                it?.data?.review?.let {
                    setImageSlider(it)
                    reviewArraySize = it.size

                }
                addLearn(it?.data?.points ?: ArrayList<String>())
//                featureListData = it.featuredItem
//                Detaildata = it.vendorList
//                adapter = ItemListAdapter(context!!,Detaildata)
                binding.rootScrollView.scrollTo(0, 0)
//                adapter.updateData(Detaildata)
//                binding.rvList.adapter = adapter
                binding.layoutLoading.visibility = View.GONE
//                hideSkimmerViews()
                if (bannerListData.size > 0) {
                    addBannerView(bannerListData)
                }
//                if (featureListData.size > 0) {
//                    addBannerFeatureView(featureListData)
//                }
            }
        })

        if(!Session.getUserID(context!!).equals("")){
            binding.root.findViewById<TextView>(R.id.tvTitle).setText("WELCOME "+Session.getSession(USER_NAME,context!!))
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

    private fun addBannerView(bannerListData: ArrayList<String>) {
        binding.bannerList.removeAllViews()
        val layoutParams =
            LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(0, 5, 20, 5)
        val imgLayoutParams = LinearLayout.LayoutParams(DisplayUtils.getWidth(activity!!)-DisplayUtils.dpToPxInt(50), 500)
//        imgLayoutParams.setMargins(0, 0, 10, 0)
        val linearLayout = LinearLayout(activity)
        linearLayout.layoutParams = layoutParams
        linearLayout.orientation = LinearLayout.HORIZONTAL
        for (i in 0 until bannerListData.size) {
            val ivItem = ImageView(activity)
            ivItem.id = i
            ivItem.layoutParams = imgLayoutParams
            ivItem.scaleType = ImageView.ScaleType.FIT_XY
            bindImageFromUrl(ivItem, bannerListData[i])
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

    override fun onResume() {
        super.onResume()

        val mAdView = binding.root.findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        val mAdView2 = binding.root.findViewById<AdView>(R.id.adView2)
        val adRequest2 = AdRequest.Builder().build()
        mAdView2.loadAd(adRequest2)

    }


    private fun addLearn(data: ArrayList<String>) {
        binding.points.removeAllViews()
        val layoutParams =
            LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
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


    inner class SliderAdapter(private val context: Context, private val imageArray: ArrayList<Review>) :
        PagerAdapter() {

        override fun getCount(): Int {
            return imageArray.size
        }

        override fun isViewFromObject(view: View, anyObject: Any): Boolean {
            return view === anyObject
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
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