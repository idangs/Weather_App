package hu.aut.android.todorecyclerviewdemo.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hu.aut.android.todorecyclerviewdemo.MainActivity
import hu.aut.android.todorecyclerviewdemo.R
import hu.aut.android.todorecyclerviewdemo.R.drawable.city
import hu.aut.android.todorecyclerviewdemo.Weather_display
import hu.aut.android.todorecyclerviewdemo.data.AppDatabase
import hu.aut.android.todorecyclerviewdemo.data.WeatherReport
import hu.aut.android.todorecyclerviewdemo.touch.CityTouchHelperAdapter
import kotlinx.android.synthetic.main.city_row.view.*
import java.util.*
import android.widget.Toast



class CityAdapter : RecyclerView.Adapter<CityAdapter.ViewHolder>, CityTouchHelperAdapter {

    companion object {
        val CITY_NAME = "CITY NAME"
    }

    var cityNames = mutableListOf<WeatherReport>()

    val context : Context


    constructor(context: Context, cityList: List<WeatherReport>) : super() {
        this.context = context
        this.cityNames.addAll(cityList)
    }

    constructor(context: Context) : super() {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.city_row, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cityNames.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = cityNames[position]

        holder.ct_name.text = city.cityName

        holder.btnDelete.setOnClickListener {
            deletecity(holder.adapterPosition)
        }

        holder.itemView.setOnClickListener { view ->
                val intent = Intent(view.context, Weather_display::class.java)
                intent.putExtra(CITY_NAME, city.cityName)
                view.context.startActivity(intent)
        }

    }



    private fun deletecity(adapterPosition: Int) {
        Thread {
            AppDatabase.getInstance(
                context).WeatherDAO().deleteCity(cityNames[adapterPosition])

            cityNames.removeAt(adapterPosition)

            (context as MainActivity).runOnUiThread {
                notifyItemRemoved(adapterPosition)
            }
        }.start()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val ct_name = itemView.city
        val btnDelete = itemView.btnDelete
    }


    fun addTodo(todo: WeatherReport) {
        cityNames.add(0, todo)
        //notifyDataSetChanged()
        notifyItemInserted(0)
    }

    override fun onDismissed(position: Int) {
        deletecity(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(cityNames, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }







}