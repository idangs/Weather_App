package hu.aut.android.todorecyclerviewdemo.data

import android.arch.persistence.room.*

@Dao
interface WeatherDAO {

    @Query("SELECT * FROM names")
    fun findAllCities(): List<WeatherReport>

    @Insert
    fun insertCity(item: WeatherReport) : Long

    @Delete
    fun deleteCity(item: WeatherReport)


}
