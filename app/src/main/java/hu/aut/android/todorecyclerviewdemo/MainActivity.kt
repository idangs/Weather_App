package hu.aut.android.todorecyclerviewdemo

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import hu.aut.android.todorecyclerviewdemo.adapter.CityAdapter
import hu.aut.android.todorecyclerviewdemo.data.AppDatabase
import hu.aut.android.todorecyclerviewdemo.data.WeatherReport
import hu.aut.android.todorecyclerviewdemo.touch.CityTouchHelperCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), CityDialog.CityHandler, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var cityAdapter: CityAdapter

    private var editIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fabAddTodo.setOnClickListener {view ->
            showAddCityDialog()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        Thread {
            val cityList = AppDatabase.getInstance(
                    this@MainActivity
            ).WeatherDAO().findAllCities()

            cityAdapter = CityAdapter(
                    this@MainActivity,
                    cityList
            )
            runOnUiThread {
                recyclerTodo.adapter = cityAdapter
                val callback = CityTouchHelperCallback(cityAdapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(recyclerTodo)
            }
        }.start()
    }

    private fun showAddCityDialog() {
        CityDialog().show(supportFragmentManager,
                "TAG_CREATE")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

     override fun todoCreated(item: WeatherReport) {
        Thread {
            val cityId = AppDatabase.getInstance(
                    this@MainActivity).WeatherDAO().insertCity(item)
            item.cityId = cityId

            runOnUiThread {
                cityAdapter.addTodo(item)
            }
        }.start()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.add_city -> {
                showAddCityDialog()
            }
            R.id.about -> {
                Toast.makeText(this, getString(R.string.creator), Toast.LENGTH_SHORT).show()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
