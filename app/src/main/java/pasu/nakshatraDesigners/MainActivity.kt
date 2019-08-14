package pasu.nakshatraDesigners

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.frag_profile.*
import nakshatraDesigners.utils.CommonFunctions
import pasu.nakshatraDesigners.adapter.NavigataionAdapter
import pasu.nakshatraDesigners.data.NavData
import pasu.nakshatraDesigners.fragments.DashboardFrag
import pasu.nakshatraDesigners.fragments.ProfileFrag
import pasu.nakshatraDesigners.signIn.SignInActivity
import pasu.nakshatraDesigners.signIn.SignupActivity
import pasu.nakshatraDesigners.utils.DialogOnClickInterface
import pasu.nakshatraDesigners.utils.Session
import pasu.nakshatraDesigners.utils.USER_NAME
import pasu.nakshatraDesigners.utils.USER_PHONE_NO
import pasu.nakshatraDesigners.utils.widgets.CustomTextview
import pub.devrel.easypermissions.EasyPermissions


const val RC_CAMERA_PERM = 123
val logoutAlert = 2

class MainActivity : AppCompatActivity(), rvItemClick, FragmentManager.OnBackStackChangedListener,
    DialogOnClickInterface {
    override fun onPositiveButtonCLick(dialog: DialogInterface, alertType: Int) {
        if (alertType == logoutAlert) {
            CommonFunctions.clearLoginSession(this@MainActivity!!)
            CommonFunctions.checkUserIdAndRedirect(this@MainActivity!!)
            finish()
        }
    }

    override fun onNegativeButtonCLick(dialog: DialogInterface, alertType: Int) {
    }

    override fun onBackStackChanged() {

    }

    fun fragmentList(fm: FragmentManager) {
        fm.registerFragmentLifecycleCallbacks(object :
            FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
                super.onFragmentPreAttached(fm, f, context)
                Log.v("FragXX1", f.getTag() ?: "")
            }

            override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
                super.onFragmentAttached(fm, f, context)
                Log.v("FragXX2", f.getTag() ?: "")
            }


            override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
                super.onFragmentStarted(fm, f)
                Log.v("FragXX6", f.getTag() ?: "")
            }

            override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                super.onFragmentResumed(fm, f)
                Log.v("FragXX7", f.getTag() ?: "")
                println("Nandhini clickedID onFragmentResumed ${f.tag} ")
//                menuIcon.tag="0"
//                menuIcon.setImageResource(R.drawable.ic_arrow_back_black_24dp)
                var data: NavData? = null
                try {
                    data = Gson().fromJson(f.tag, NavData::class.java)
                } catch (e: Exception) {
                }
                if (data != null)
                    if (data?.id == -6) {
                        menuIcon.tag = "1"
                        menuIcon.setImageResource(R.drawable.ic_menu)
                        title.setText(getString(R.string.menu_dashboard))
                        onlineclasses.visibility = View.VISIBLE
                    } else {
                        title.setText(data?.name)
                        onlineclasses.visibility = View.GONE
                        menuIcon.setImageResource(R.drawable.ic_arrow_back_black_24dp)
                        menuIcon.tag = "0"
                    }
//
                adapter.selectedItem = (f?.tag ?: "-6")
                adapter.notifyDataSetChanged()
            }

            override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
                super.onFragmentPaused(fm, f)
                Log.v("FragXX8", f.getTag() ?: "")
            }

            override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
                super.onFragmentStopped(fm, f)
                Log.v("FragXX9", f.getTag() ?: "")
            }

            override fun onFragmentSaveInstanceState(
                fm: FragmentManager,
                f: Fragment,
                outState: Bundle
            ) {
                super.onFragmentSaveInstanceState(fm, f, outState)
                Log.v("FragXX10", f.getTag() ?: "")
            }

            override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
                super.onFragmentViewDestroyed(fm, f)
                Log.v("FragXX11", f.getTag() ?: "")
            }

            override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
                super.onFragmentDestroyed(fm, f)
                Log.v("FragXX12", f.getTag() ?: "")
            }

            override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
                super.onFragmentDetached(fm, f)
                Log.v("FragXX13", f.getTag() ?: "")
                var data: NavData? = null
                try {
                    data = Gson().fromJson(f.tag, NavData::class.java)
                } catch (e: Exception) {
                }
                if (data?.id != -6 && data?.id != 1) {
                    println("frag back13 ${data?.id}")
                    menuIcon.tag = "1"
                    onlineclasses.visibility = View.VISIBLE
                    menuIcon.setImageResource(R.drawable.ic_menu)
                    title.setText(getString(R.string.menu_dashboard))
                }

//
            }
        }, true)
    }

    override fun itemClicked(position: Int, data: NavData) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
//        println("Nandhini clickedID $position $data.id")
//        if (data.id == 1) {
//            if (Session.getUserID(this@MainActivity).equals("")) {
//                startActivity(Intent(this@MainActivity, SignInActivity::class.java))
//            } else {
//                supportFragmentManager.beginTransaction().add(
//                    R.id.nav_host_frag,
//                    Certificates()
//                    , "" + data.id
//                ).addToBackStack(null).commit()
//            }
//        } else if (data.id == -6) {
//
//        } else if (data.id == 1000) {
//            supportFragmentManager.beginTransaction().add(
//                R.id.nav_host_frag,
//                ContactUsFragment()
//                , "" + data.id
//            ).addToBackStack(null).commit()
//        } else {
//            var fragment = CertificateDetailFrag()
//            var bundle = Bundle()
//            bundle.putString(WEB_LOAD_URL, data.url)
//            fragment.arguments = bundle
//            supportFragmentManager.beginTransaction().add(
//                R.id.nav_host_frag,
//                fragment
//                , "" + data.id
//            ).addToBackStack(null).commit()
////            onBackPressed()
//        }
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var adapter: NavigataionAdapter
    private lateinit var menuIcon: AppCompatImageView
    private lateinit var title: CustomTextview
    public lateinit var onlineclasses: CustomTextview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        drawerLayout = findViewById(R.id.drawer_layout)
//        navController = Navigation.findNavController(this, R.id.nav_host_frag)
//        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)


        // Set up ActionBar
        toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)
        menuIcon = toolbar.findViewById(R.id.menu_icon)
        title = toolbar.findViewById(R.id.toolbarTitle)
        onlineclasses = findViewById(R.id.onlineclasses)

        menuIcon.setOnClickListener {
            if (menuIcon.getTag() == "0")
                onBackPressed() else {
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START)
                }

            }
        }
        val navigationView = findViewById<RecyclerView>(R.id.nav_rv_list)
        var fm = supportFragmentManager
        fragmentList(fm)
        fm.beginTransaction().add(
            R.id.nav_host_frag,
            DashboardFrag(),
            Gson().toJson(NavData(getString(R.string.menu_dashboard), -6, ""))
        ).commit()
        navigationView.layoutManager = LinearLayoutManager(this@MainActivity)
        adapter =
            NavigataionAdapter(
                this@MainActivity,
                Session.getNavData(this@MainActivity),
                this@MainActivity
            )

        navigationView.adapter = adapter
//        navigationView.addItemDecoration(DividerItemDecoration(navigationView.getContext(), DividerItemDecoration.VERTICAL));
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        val userLay = findViewById<View>(R.id.userLay)
        val guestLay = findViewById<View>(R.id.guestLay)
        val userName = findViewById<TextView>(R.id.userName)
        val userPhNo = findViewById<TextView>(R.id.userPhNo)
        val loginButton = findViewById<View>(R.id.login)
        val registerButton = findViewById<View>(R.id.register)
        val logoutButton = findViewById<View>(R.id.logOut)

        if (Session.getUserID(this@MainActivity).equals("")) {
            guestLay.visibility = View.VISIBLE
            userLay.visibility = View.GONE
            loginButton.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainActivity,
                        SignInActivity::class.java
                    )
                )
            }
            registerButton.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainActivity,
                        SignupActivity::class.java
                    )
                )
            }
            logoutButton.visibility = View.GONE

        } else {
            logOut.visibility = View.VISIBLE
            logoutButton.setOnClickListener {
                CommonFunctions.alertDialog(
                    this@MainActivity,
                    this,
                    getString(R.string.logout_msg),
                    getString(R.string.ok),
                    getString(R.string.cancel),
                    false, logoutAlert, getString(R.string.confirm_logout)
                )
            }
            guestLay.visibility = View.GONE
            userLay.visibility = View.VISIBLE
            userLay.setOnClickListener {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                supportFragmentManager.beginTransaction().add(
                    R.id.nav_host_frag,
                    ProfileFrag()
                    , Gson().toJson(NavData(getString(R.string.profile), -8, ""))
                ).addToBackStack(null).commit()
            }
            userName.text = Session.getSession(USER_NAME, this@MainActivity)
            userPhNo.text = Session.getSession(USER_PHONE_NO, this@MainActivity)

        }

    }

    fun getToolBar(): Toolbar {
        return toolbar
    }

//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    private fun hasLocationPermission(): Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)
    }


}

