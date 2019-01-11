package hu.aut.android.todorecyclerviewdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import hu.aut.android.todorecyclerviewdemo.adapter.CityAdapter
import hu.aut.android.todorecyclerviewdemo.data_classes.Base
import hu.aut.android.todorecyclerviewdemo.network.Weather_API
import kotlinx.android.synthetic.main.activity_weather_display.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class Weather_display : AppCompatActivity() {

    private val HOST_URL = "https://api.openweathermap.org/"

    lateinit var city: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_display)

        val retrofit = Retrofit.Builder()
                .baseUrl(HOST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val calendar = Calendar.getInstance()
        val weatherAPI = retrofit.create(Weather_API::class.java)

        if (intent.hasExtra(CityAdapter.CITY_NAME)) {
            city = intent.getStringExtra(CityAdapter.CITY_NAME)
            city_nam.text = city
        }


            val weatherCall = weatherAPI.getWeather(city, "metric",
                    "bba944ec4bbf93006868b7c48bc890a5")

            weatherCall.enqueue(object : Callback<Base> {
                override fun onResponse(call: Call<Base>?, response: Response<Base>) {
                    val weatherval = response.body()

                    val min_temp = weatherval?.main?.temp_min?.toString()
                    val max_temp = weatherval?.main?.temp_max?.toString()
                    val winds = weatherval?.wind?.speed?.toString()
                    val cloudss = weatherval?.clouds?.all?.toString()
                    val rise =  weatherval?.sys?.sunrise?.toString()
                    val set = weatherval?.sys?.sunset?.toString()
                    val humid = weatherval?.main?.humidity?.toString()
                    val press = weatherval?.main?.pressure?.toString()

                    min_temperature.text = getString(R.string.head_mintemp, min_temp)
                    max_temperature.text = getString(R.string.head_maxtemp, max_temp)

                    wind.text = getString(R.string.wind_speed, winds)
                    clouds.text = getString(R.string.cloud_num, cloudss)

                    val sunRise = weatherval?.sys?.sunrise!!.toLong()
                    calendar.setTimeInMillis(sunRise * 1000)
                    val store = SimpleDateFormat("HH:mm").format(calendar.time)
                    sunrise.text = getString(R.string.rising,store)

                    val sunSet = weatherval?.sys?.sunset!!.toLong()
                    calendar.setTimeInMillis(sunSet * 1000)
                    sunset.text = getString(R.string.set, SimpleDateFormat("HH:mm").format(calendar.time))

                    humidity.text = getString(R.string.humidity, humid)

                    pressure.text = getString(R.string.pressure, press)

                    Glide.with(this@Weather_display).load(
                            ("https://openweathermap.org/img/w/" + response.body()?.weather?.get(0)?.icon + ".png"))
                            .into(ivWeather)

                }

                override fun onFailure(call: Call<Base>, t: Throwable) {
                   city_nam.text = t.message
                }

            })


        }
    }

