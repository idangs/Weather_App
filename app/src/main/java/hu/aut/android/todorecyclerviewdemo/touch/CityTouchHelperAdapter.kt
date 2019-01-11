package hu.aut.android.todorecyclerviewdemo.touch

interface CityTouchHelperAdapter {
    fun onDismissed(position: Int)
    fun onItemMoved(fromPosition: Int, toPosition: Int)
}