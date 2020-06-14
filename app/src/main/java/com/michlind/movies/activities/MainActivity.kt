package com.michlind.movies.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.zxing.integration.android.IntentIntegrator
import com.michlind.movies.R
import com.michlind.movies.database.DataBaseHelper
import com.michlind.movies.fragments.ListFragment
import com.michlind.movies.model.Movie
import com.michlind.movies.model.ViewModel
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), ListFragment.OnFragmentInteractionListener {

    companion object {
        private val TAG: String? = MainActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        //ViewModel provider
        val viewModel: ViewModel = ViewModelProvider(this).get(
            ViewModel::class.java
        )

        lifecycleScope.launch {
            viewModel.movies = DataBaseHelper.getAllMoviesFromDB(this@MainActivity)!!

            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, ListFragment(), ListFragment.FRAGMENT_TAG)
                    .commitNow()
            }
        }
    }

    private fun parseQrData(contents: String) {

        val jsonObject: JsonObject = JsonParser().parse(contents).getAsJsonObject()

        //Parse movie from Json
        val movie = Gson().fromJson(jsonObject, Movie::class.java)
        lifecycleScope.launch {
            val isExist = DataBaseHelper.addMovieToDB(this@MainActivity, movie)

            if (isExist) {
                showSnackBar(getString(R.string.movie_exist))
            } else {
                lifecycleScope.launch {
                    val movies = DataBaseHelper.getAllMoviesFromDB(this@MainActivity)
                    val listFragment: ListFragment =
                        supportFragmentManager.findFragmentByTag(ListFragment.FRAGMENT_TAG) as ListFragment
                    listFragment.refreshAdapter(movies)
                }
            }
        }
    }

    //Show snackbar
    private fun showSnackBar(text: String) {
        val parentLayout: View = findViewById(android.R.id.content)
        Snackbar.make(parentLayout, text, Snackbar.LENGTH_LONG)
            .show()
    }

    //On add button click
    override fun onFloatingActionButtonClick() {
        val scanner = IntentIntegrator(this)
        scanner.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            var result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    Log.e(TAG, "QR scan error")
                } else {
                    parseQrData(result.contents)
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }
}

