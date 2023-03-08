package pasu.nakshatraDesigners.fragments


import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import pasu.nakshatraDesigners.R
import pasu.nakshatraDesigners.data.NavData
import pasu.nakshatraDesigners.data.OnlineClasses
import pasu.nakshatraDesigners.databinding.FragmentSelectCoursesBinding
import pasu.nakshatraDesigners.utils.Session
import pasu.nakshatraDesigners.utils.widgets.CustomButton


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SelectCoursesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SelectCoursesFragment : Fragment() {
    private lateinit var binding: FragmentSelectCoursesBinding
    private var onlineClassesList: ArrayList<OnlineClasses>? = null

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param onlineClassesList Parameter 1.
         * @return A new instance of fragment SelectCoursesFragment.
         */
        @JvmStatic
        fun newInstance() =
            SelectCoursesFragment().apply {
//                val listType = object : TypeToken<ArrayList<OnlineClasses>>() {
//
//                }.type
//                print("onlineSelection ${Session.getSession("onlineSelection",context)}")
//                val onlineClassesList: ArrayList<OnlineClasses> =
//                    Gson().fromJson(pasu.nakshatraDesigners.utils.Session.getSession("onlineSelection",context), listType)
                arguments = Bundle().apply {
                    DashboardFrag.onlineClassesList?.let {
                        putParcelableArrayList(ARG_PARAM1, DashboardFrag.onlineClassesList)
                    }

                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            onlineClassesList = it.getParcelableArrayList(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectCoursesBinding.inflate(inflater, container, false)


        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)


        val adRequest2 = AdRequest.Builder().build()
        binding.adView1.loadAd(adRequest2)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        onlineClassesList?.let { createCourseButtons(it) }
    }

    private fun createCourseButtons(onlineClassesList: ArrayList<OnlineClasses>) {
        binding.buttonsLayout.removeAllViews()
        onlineClassesList.forEach {
            binding.buttonsLayout.addView(CustomButton(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    val margin = resources.getDimensionPixelOffset(R.dimen.dp8)
                    setMargins(margin, margin, margin, margin)
                }
                id = it.id.toInt()
                text = it.buttonname
                gravity = Gravity.CENTER
                setType(getString(R.string.postive_button))
                setOnClickListener { view ->
                    requireActivity().supportFragmentManager.beginTransaction().add(
                        R.id.nav_host_frag,
                        Certificates.newInstance(it.apiurl)
                        , Gson().toJson(NavData(getString(R.string.online_classess), 1, ""))
                    ).addToBackStack(null).commit()
                }
            })
        }
    }
}
