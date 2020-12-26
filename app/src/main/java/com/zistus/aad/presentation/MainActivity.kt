package com.zistus.aad.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.zistus.aad.R
import com.zistus.aad.data.model.Entity
import com.zistus.aad.databinding.ActivityMainBinding
import com.zistus.aad.databinding.ProcessFailedDialogBinding
import com.zistus.aad.utils.ViewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel
    private val progressFragment = ProgressFragment.instance()
    var eventAndStateJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Needed a factory to inject context into MainRepoImpl(used context of internet check)
        viewModel = ViewModelFactory(this).create(MainViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        observeEventAndState()

        binding.startButton.setOnClickListener {
            val number = binding.cardNumberEntry.text.toString()
            if (number.isNotBlank() && number.length > 6) {
                clearDetailView()
                viewModel.queryCard(number)
            } else {
                binding.cardNumberEntryLayout.error = "Enter first 6-9 digits of card!"
            }
        }

        // Auto do fetching when the user has put in all 9 digits
        binding.cardNumberEntry.doAfterTextChanged { p0 ->
            if (p0?.length == 9) viewModel.queryCard(p0.toString())
        }
    }

    // Update the view with the card detail fetched
    private fun updateDetailView(card: Entity.Card) {
        val emptyValue = "Not Available"

        binding.firstData.text = resources.getString(
            R.string.card_type_data,
            card.type?.capitalize() ?: emptyValue,
            card.scheme?.capitalize() ?: emptyValue
        )
        binding.secondData.text = resources.getString(
            R.string.card_bank_data,
            "${card.bank?.name?.capitalize() ?: emptyValue}, ${card.bank?.city?.capitalize() ?: ""}",
            card.country?.name?.capitalize() ?: emptyValue
        )
        binding.thirdData.text = "Number Length: ${card.number?.length ?: emptyValue}"
        binding.fourthData.text = if (card.prepaid) "Prepaid" else "Not Prepaid"
    }

    // Update the view with the card detail fetched
    private fun clearDetailView() {
        if (binding.cardNumberEntryLayout.isErrorEnabled) binding.cardNumberEntryLayout.error = null
        binding.firstData.text = ""
        binding.secondData.text = ""
        binding.thirdData.text = ""
        binding.fourthData.text = ""
    }

    // Observes the live-data states from the viewModel and execute as necessary
    private fun observeEventAndState() {
        eventAndStateJob = lifecycleScope.launch {
            viewModel.dataState.observe(
                this@MainActivity,
                { viewState ->
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
                }
            )

            viewModel.viewState.observe(
                this@MainActivity,
                { viewState ->
                    viewState.card?.let {
                        Log.d(javaClass.simpleName, "Fetched card $it")
                        updateDetailView(it)
                    }
                }
            )
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
        val dialogBinding = ProcessFailedDialogBinding.inflate(layoutInflater)
        val view = dialogBinding.root
        dialogBinding.failureMsg.text = msg
        val errorDialog = MaterialAlertDialogBuilder(this)
            .setView(view)
            .setCancelable(false)
            .show()
        dialogBinding.okButton.setOnClickListener {
            if (errorDialog.isShowing) errorDialog.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        eventAndStateJob?.cancel()
    }
}
