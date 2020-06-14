package com.michlind.movies.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.michlind.movies.R
import com.michlind.movies.api.ApiService
import com.michlind.movies.database.DataBaseHelper
import com.michlind.movies.model.DefaultData
import com.michlind.movies.model.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private var compositeDisposable: CompositeDisposable? = null


class SplashScreen : AppCompatActivity() {

    companion object {
        private val TAG: String? = SplashScreen::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        //context = this
        compositeDisposable = CompositeDisposable()

        lifecycleScope.launch {
            val dbIsEmpty = DataBaseHelper.isDbEmpty(this@SplashScreen)

            if (dbIsEmpty) {
                Log.d(TAG,"Load remotely")
                loadData()
            } else {
                Log.d(TAG,"Load locally")
                //Show splash screen for x seconds
                Handler().postDelayed({
                    startMainActivity()
                }, 500)

            }
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    //Load API data using Retrofit and RXJava
    private fun loadData() {

        val requestInterface = Retrofit.Builder()
            .baseUrl(DefaultData.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(ApiService::class.java)

        compositeDisposable?.add(
            requestInterface.getMovies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse)
        )
    }

    private fun handleResponse(movieList: List<Movie>) {

        lifecycleScope.launch {
             DataBaseHelper.addAllMovies(this@SplashScreen,movieList)
                startMainActivity()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }
}

