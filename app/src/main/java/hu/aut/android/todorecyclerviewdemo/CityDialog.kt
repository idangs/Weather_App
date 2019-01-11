package hu.aut.android.todorecyclerviewdemo

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.EditText
import hu.aut.android.todorecyclerviewdemo.data.WeatherReport
import kotlinx.android.synthetic.main.add_city.view.*
import java.lang.RuntimeException

open class CityDialog : DialogFragment() {

    interface CityHandler {
        fun todoCreated(item: WeatherReport)
    }

    private lateinit var cityHandler: CityHandler

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is CityHandler) {
            cityHandler = context
        } else {
            throw RuntimeException(
                "The activity does not implement the CityHandlerInterface")
        }
    }


    private lateinit var cityName: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(getString(R.string.add_city))

        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.add_city, null
        )

        cityName = rootView.city_nam
        builder.setView(rootView)
        builder.setPositiveButton(getString(R.string.ok)) {
                dialog, witch -> // empty
        }

        builder.setNegativeButton(getString(R.string.cancel)) {
            dialog, witch ->  dialog.dismiss()
        }

        return builder.create()
    }


    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (cityName.text.isNotEmpty()) {
                handleTodoCreate()
                dialog.dismiss()
            } else {
                cityName.error = getString(R.string.empty_error)
            }
        }
    }

    private fun handleTodoCreate() {
        cityHandler.todoCreated(
            WeatherReport(
                null,
                cityName.text.toString()
            )
        )
    }


}