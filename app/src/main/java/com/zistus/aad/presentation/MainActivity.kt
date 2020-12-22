package com.zistus.aad.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.zistus.aad.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private val progressFragment = ProgressFragment.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        setContentView(R.layout.activity_main)
        observeEventAndState()

        start_button?.setOnClickListener {
            val number = card_number_entry.text.toString()
            if (number.isNotBlank() && number.length > 6) {
                viewModel.queryCard(number)
            }
        }
    }

    // Observes the livedata states from the viewModel and execute as necessary
    private fun observeEventAndState() {
        viewModel.dataState.observe(this, { viewState->
            viewState.loading?.let {
                // show loading if loading state is true
                Log.d(javaClass.simpleName, "Loading: $it")
                toShowDialog(it, progressFragment)
            }

            viewState.error?.let {
                // show error when error is returned
                Toast.makeText(this, "Failed: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
            viewState.data?.let { event->
                event.getContentIfNotHandled()?.let { state->
                    viewModel.setViewState(viewState = state)
                }
            }
        })

        viewModel.viewState.observe(this, { viewState->
            viewState.card?.let {
                Log.d(javaClass.simpleName, "Fetched card $it")
            }
        })
    }

    private fun toShowDialog(toShow: Boolean, progressFragment: ProgressFragment) {
        if (toShow) {
            inflateProgressDialog(progressFragment)
        }else {
            removeProgressDialog(progressFragment)
        }
    }

    private fun inflateProgressDialog(frgment: ProgressFragment) {
        frgment.show(supportFragmentManager, ProgressFragment.TAG)
    }

    private fun removeProgressDialog(frgment: ProgressFragment) {
        Log.d(javaClass.simpleName, "Dismiss progress, Progress visible: ${frgment.isVisible}")
        if (frgment.isVisible) {
            frgment.dismiss()
        }
    }

}
