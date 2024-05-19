package com.example.ck_4_navigation_drawer

import android.os.Bundle
import android.provider.Settings.Global
import android.view.Menu
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
//import com.example.ck_4_navigation_drawer.account.nickName
import com.example.ck_4_navigation_drawer.databinding.ActivityMainBinding
import com.example.ck_4_navigation_drawer.databinding.NavHeaderMainBinding
import io.ktor.client.request.*
import kotlinx.coroutines.*
import kotlin.properties.Delegates



class MainActivity : AppCompatActivity()
{

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        val navHeaderBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0))
        val textViewName: TextView = navHeaderBinding.textViewName
        val textViewEmail: TextView = navHeaderBinding.textViewEmail

        account.UserInfo = { ->
            val db = AppDatabase.get(this)
            runBlocking {
                var nickname = listOf<String>()
                var email = listOf<String>()
                val job = CoroutineScope(Dispatchers.IO).launch() {
                nickname = withContext(Dispatchers.IO) { db.userDao().getNickName() }
                email = withContext(Dispatchers.IO) { db.userDao().getEmail() }
                }
                job.join()
                if (nickname.isEmpty()) {
                    textViewName.text = "Гость"
                    textViewEmail.text = ""
                } else {
                    textViewName.text = nickname[0]
                    textViewEmail.text = email[0]
                }
            }
        }

        account.UserInfo()



        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

//        binding.appBarMain.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_favourites, R.id.nav_authorization
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


    override fun onSupportNavigateUp(): Boolean
    {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}