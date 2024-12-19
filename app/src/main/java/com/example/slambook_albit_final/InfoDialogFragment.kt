package com.example.slambook_albit_final

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.slambook_albit_final.databinding.DialogInfoBinding

class InfoDialogFragment : DialogFragment() {

    private lateinit var binding: DialogInfoBinding

    // newInstance method to create a new instance of InfoDialogFragment
    companion object {
        fun newInstance(): InfoDialogFragment {
            return InfoDialogFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the dialog width to match the parent (screen width)
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        binding.backButton.setOnClickListener {
            dismiss()
        }
    }
}
