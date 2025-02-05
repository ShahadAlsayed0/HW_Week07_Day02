package com.sumaya.hw_week06_day05.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sumaya.hw_week06_day05.R

class MainActivity : AppCompatActivity() {
    private val viewModel: MainVM by lazy {
        ViewModelProvider(this)[MainVM::class.java]
    }
    private lateinit var sharedPreferences: SharedPreferences
    private val SHARED_KEY = "lastSearch"
    private val TAG = "search view"
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recyclerView = findViewById(R.id.rvTMDB)
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        sharedPreferences =
            this.getSharedPreferences("SearchSharedPreference", Context.MODE_PRIVATE)
        loadFlickerImages()

    }

    fun loadFlickerImages(query: String? = null) {
        viewModel.getMovies(query).observe(this, {
            if (query.isNullOrEmpty()) {
                recyclerView.adapter = Adapter(it.results, viewModel)
            } else {
                recyclerView.swapAdapter(Adapter(it.results, viewModel), false)
            }
            Log.d(" main response", "helo")
        })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchIcon: MenuItem = menu!!.findItem(R.id.app_bar_search)
        val searchView = searchIcon.actionView as SearchView
        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    Log.d(TAG, "QueryTextSubmit: $query")
                    val sharedPref =
                        sharedPreferences.getString(SHARED_KEY, "This was your first search!")
                    sharedPreferences.edit().putString(SHARED_KEY, query).apply()
                    Toast.makeText(context, "Your last search was $sharedPref", Toast.LENGTH_LONG)
                        .show()
                    loadFlickerImages(query!!.trim())
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.d(TAG, "QueryTextChange: $newText")
                    return false
                }


            })

        }



        return super.onCreateOptionsMenu(menu)
    }

}