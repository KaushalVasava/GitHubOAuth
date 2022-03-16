package com.lahsuak.apps.githuboauth.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.lahsuak.apps.githuboauth.R
import com.lahsuak.apps.githuboauth.databinding.ActivityMainBinding
import com.lahsuak.apps.githuboauth.ui.viewmodel.RepositoryViewModel
import com.lahsuak.apps.githuboauth.utils.Constants.ACCESS_TOKEN
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    @Inject
    lateinit var sharedPref: SharedPreferences
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val viewModel: RepositoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val mDrawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.loginFragment),
            binding.drawerLayout
        )

        val navHostFragment =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.main_nav_graph)

        var accessToken = sharedPref.getString(ACCESS_TOKEN, null)

        val navView = binding.navDrawer.getHeaderView(0)
        val profileImage = navView.findViewById<ShapeableImageView>(R.id.ivProfile)
        val name = navView.findViewById<TextView>(R.id.txtName)

        if (accessToken.isNullOrBlank()) {
            graph.setStartDestination(R.id.loginFragment)
            navHostFragment.navController.graph = graph
        } else {
            graph.setStartDestination(R.id.repositoryFragment)
            val bundle = Bundle()
            bundle.putString("accessToken", accessToken)
            navHostFragment.navController.setGraph(graph, bundle)
        }

        //set controller for navigation between fragments
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navDrawer.setupWithNavController(navController)

        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                accessToken = sharedPref.getString(ACCESS_TOKEN, null)
                if (accessToken != null) {
                    viewModel.getUserData(accessToken!!)
                    viewModel.userData.observe(this@MainActivity) {
                        name.text = it.name
                        Glide.with(this@MainActivity).load(it.imageUrl)
                            .error(R.drawable.img_github).into(profileImage)
                    }
                }
            }

            override fun onDrawerOpened(drawerView: View) {

            }

            override fun onDrawerClosed(drawerView: View) {}

            override fun onDrawerStateChanged(newState: Int) {}
        })
        mDrawerToggle.syncState()
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}