package hu.aut.android.todorecyclerviewdemo.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable



@Entity(tableName = "names")
data class WeatherReport(
        @PrimaryKey(autoGenerate = true) var cityId: Long?,
        @ColumnInfo(name = "cityName") var cityName: String
) : Serializable
