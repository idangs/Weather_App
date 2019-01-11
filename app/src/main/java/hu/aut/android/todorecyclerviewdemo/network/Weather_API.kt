package hu.aut.android.todorecyclerviewdemo.network


import hu.aut.android.todorecyclerviewdemo.data_classes.Base
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface Weather_API{
    @GET("data/2.5/weather")
    fun getWeather(@Query("q") city: String,
                   @Query("units") units: String,
                   @Query("appid") appid: String): Call<Base>
}