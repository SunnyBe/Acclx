package com.zistus.aad.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.zistus.aad.R
import com.zistus.aad.data.model.Entity
import com.zistus.aad.utils.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.process_failed_dialog.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private val progressFragment = ProgressFragment.instance()
    var eventAndStateJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Needed a factory to inject context into MainRepoImpl(used context of internet check)
        viewModel = ViewModelFactory(this).create(MainViewModel::class.java)
        setContentView(R.layout.activity_main)
        observeEventAndState()

        start_button?.setOnClickListener {
            val number = card_number_entry.text.toString()
            if (number.isNotBlank() && number.length > 6) {
                clearDetailView()
                viewModel.queryCard(number)
            }else {
                card_number_entry_layout.error = "Enter first 6-9 digits of card!"
            }
        }

        // Auto do fetching when the user has put in all 9 digits
        card_number_entry?.doAfterTextChanged { p0->
            if (p0?.length == 9) viewModel.queryCard(p0.toString())
        }
    }

    // Update the view with the card detail fetched
    private fun updateDetailView(card: Entity.Card) {
        val emptyValue = "Not Available"

        first_data?.text = resources.getString(
            R.string.card_type_data,
            card.type?.capitalize()?:emptyValue,
            card.scheme?.capitalize()?:emptyValue
        )
        second_data?.text = resources.getString(
            R.string.card_bank_data,
            "${card.bank?.name?.capitalize()?:emptyValue}, ${card.bank?.city?.capitalize()?:""}",
            card.country?.name?.capitalize()?:emptyValue
        )
        third_data?.text = "Number Length: ${card.number?.length?:emptyValue}"
        fourth_data?.text = if (card.prepaid) "Prepaid" else "Not Prepaid"
    }

    // Update the view with the card detail fetched
    private fun clearDetailView() {
        if (card_number_entry_layout.isErrorEnabled) card_number_entry_layout?.error = null
        first_data?.text = ""
        second_data?.text = ""
        third_data?.text = ""
        fourth_data?.text = ""
    }

    // Observes the live-data states from the viewModel and execute as necessary
    private fun observeEventAndState() {
        eventAndStateJob = lifecycleScope.launch {
            viewModel.dataState.observe(this@MainActivity, { viewState ->
                viewState.loading?.let {
                    // show loading if loading state is true
                    Log.d(javaClass.simpleName, "Loading: $it")
                    toShowDialog(it, progressFragment)
                }

                viewState.error?.let {
                    // show error when error is returned
                    runOnUiThread {
                        inflateErrorDialog(if (it !is HttpException) it.message else "Failed to fetch card!")
                    }
                }
                viewState.data?.let { event ->
                    event.getContentIfNotHandled()?.let { state ->
                        viewModel.setViewState(viewState = state)
                    }
                }
            })

            viewModel.viewState.observe(this@MainActivity, { viewState ->
                viewState.card?.let {
                    Log.d(javaClass.simpleName, "Fetched card $it")
                    updateDetailView(it)
                }
            })
        }
    }

    // Show dialog base on the toShow value
    private fun toShowDialog(toShow: Boolean, progressFragment: ProgressFragment) {
        if (toShow) {
            inflateProgressDialog(progressFragment)
        } else {
            removeProgressDialog(progressFragment)
        }
    }

    // Inflate the progress dialog
    private fun inflateProgressDialog(frgment: ProgressFragment) {
        frgment.show(supportFragmentManager, ProgressFragment.TAG)
    }

    // Dismiss the progress dialog
    private fun removeProgressDialog(frgment: ProgressFragment) {
        Log.d(javaClass.simpleName, "Dismiss progress, Progress visible: ${frgment.isVisible}")
        if (frgment.isVisible) {
            frgment.dismiss()
        }
    }

    private fun inflateErrorDialog(msg: String?) {
        val view = LayoutInflater.from(this).inflate(R.layout.process_failed_dialog, null, false)
        view.failure_msg?.text = msg
        val errorDialog = MaterialAlertDialogBuilder(this)
            .setView(view)
            .setCancelable(false)
            .show()
        view.ok_button?.setOnClickListener {
            if (errorDialog.isShowing) errorDialog.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        eventAndStateJob?.cancel()
    }
}
