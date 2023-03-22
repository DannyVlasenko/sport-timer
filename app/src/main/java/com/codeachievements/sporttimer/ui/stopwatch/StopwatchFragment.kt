package com.codeachievements.sporttimer.ui.stopwatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.codeachievements.sporttimer.databinding.FragmentStopwatchBinding

class StopwatchFragment : Fragment() {

    private var _binding: FragmentStopwatchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val stopwatchViewModel = ViewModelProvider(this)[StopwatchViewModel::class.java]
            .apply { timeText.observe(viewLifecycleOwner){binding.timeTextView.text = it} }
            .apply {
                startStopButtonTextId.observe(viewLifecycleOwner){
                    binding.startStopButton.text = context?.getString(it)
                }
            }
        _binding = FragmentStopwatchBinding.inflate(inflater, container, false)
            .apply { startStopButton.setOnClickListener{stopwatchViewModel.onStartStopClicked()} }
            .apply { resetButton.setOnClickListener{stopwatchViewModel.onResetClicked()} }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}