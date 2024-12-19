package com.example.slambook_albit_final

import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.slambook_albit_final.databinding.DialogEditProfileBinding

class EditProfileDialogFragment : DialogFragment() {

    private lateinit var binding: DialogEditProfileBinding
    private var userData: UserData? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the dialog width to match the parent (screen width)
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Access the first user data from the userDataList in Step1Activity
        userData = Step1Activity.userDataList.firstOrNull()

        // If userData is available, populate the fields
        userData?.let { data ->
            binding.fullNameText.setText(data.fullName)
            binding.nickNameText.setText(data.nickName)
            binding.genderText.setText(data.gender)
            binding.dayText.setText(data.birthDay)
            binding.monthText.setText(data.birthMonth)
            binding.yearText.setText(data.birthYear)
            binding.statusText.setText(data.status)
            binding.emailText.setText(data.email)
            binding.contactText.setText(data.contact)
            binding.addressText.setText(data.address)
        }

        // Set fields to be editable
        setEditableFields()

        binding.save.setOnClickListener {
            val fullName = binding.fullNameText.text.toString().trim()
            val nickName = binding.nickNameText.text.toString().trim()

            val gender = binding.genderText.text.toString().trim().let {
                when (it.lowercase()) {
                    "male" -> "Male"
                    "female" -> "Female"
                    else -> "Male" // Default value if gender is invalid
                }
            }

            var day = binding.dayText.text.toString().trim()
            var month = binding.monthText.text.toString().trim()
            val year = binding.yearText.text.toString().trim()
            var status = binding.statusText.text.toString().trim()
            val email = binding.emailText.text.toString().trim()
            val contactNumber = binding.contactText.text.toString().trim()
            val address = binding.addressText.text.toString().trim()

            status = status.lowercase().replaceFirstChar { it.uppercase() }

            if (day.length == 1) {
                day = "0$day"
            }

          if (year.toIntOrNull() !in 1900..2019) {
                Toast.makeText(context, "Age must be at least 5 years old.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            month = month.lowercase().replaceFirstChar { it.uppercase() }

            if (userData?.fullName == fullName &&
                userData?.nickName == nickName &&
                userData?.gender == gender &&
                userData?.status == status &&
                userData?.email == email &&
                userData?.contact == contactNumber &&
                userData?.address == address &&
                userData?.birthDay == day &&
                userData?.birthMonth == month &&
                userData?.birthYear == year
            ) {
                Toast.makeText(context, "Nothing changed.", Toast.LENGTH_SHORT).show()
                dismiss()
                return@setOnClickListener
            }

            val validationResult = validateInputs(
                fullName, nickName, gender, day, month, year, status, email, contactNumber, address
            )
            if (validationResult != null) {
                Toast.makeText(context, validationResult, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            userData?.let { user ->
                user.fullName = fullName
                user.nickName = nickName
                user.gender = gender
                user.birthDay = day
                user.birthMonth = month
                user.birthYear = year
                user.status = status
                user.email = email
                user.contact = contactNumber
                user.address = address


                Toast.makeText(context, "User data updated.", Toast.LENGTH_SHORT).show()

                // Notify CompletedActivity
                (activity as? CompletedActivity)?.onProfileUpdated(
                    fullName, nickName, gender, "$month $day $year", status, email, contactNumber, address
                )

                dismiss() // Close the dialog after saving
            } ?: run {
                Toast.makeText(context, "User data is not available.", Toast.LENGTH_SHORT).show()
            }
        }



        // Cancel button dismisses the dialog
        binding.cancel.setOnClickListener {
            dismiss()
        }

        // Apply input filter to contact number for 11 digits and numeric input only
        val contactInputFilter = InputFilter.LengthFilter(11) // Limit input length to 11 digits
        binding.contactText.filters = arrayOf(contactInputFilter, InputFilter { source, start, end, dest, dstart, dend ->
            if (source.all { it.isDigit() }) null else "" // Allow only digits
        })
    }

    private fun setEditableFields() {
        val fields = listOf(
            binding.fullNameText to "Full Name",
            binding.nickNameText to "Nickname",
            binding.genderText to "Gender",
            binding.dayText to "Day",
            binding.monthText to "Month",
            binding.yearText to "Year",
            binding.statusText to "Status",
            binding.emailText to "Email",
            binding.contactText to "Contact",
            binding.addressText to "Address"
        )
        fields.forEach { (editText, label) ->
            setEditable(editText, label)
        }
    }

    private fun setEditable(editText: EditText, label: String) {
        // Store the initial value of the EditText to restore it if needed
        val initialText = editText.text.toString()

        editText.setOnClickListener {
            val currentValue = editText.text.toString().trim()
            editText.setText(currentValue) // Make value editable
            editText.setSelection(currentValue.length) // Place cursor at the end
        }

        // Store the current value before losing focus to restore if needed
        editText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                // User focuses on the field, no need to change anything here
            } else {
                val currentValue = (v as EditText).text.toString().trim()
                if (currentValue.isEmpty()) {
                    // If the field is empty after losing focus, restore the initial value
                    editText.setText(initialText)
                }
            }
        }
    }


    private fun validateInputs(
        fullName: String, nickName: String, gender: String?, day: String, month: String,
        year: String, status: String, email: String, contactNumber: String, address: String
    ): String? {
        // Check for empty fields (except birthday)
        if (listOf(fullName, nickName, status, email, contactNumber, address).any { it.isEmpty() }) {
            return "Please fill all fields."
        }

        // Gender validation
        if (gender == null || gender.lowercase() !in listOf("male", "female")) {
            return "Please enter a valid gender ('Male' or 'Female')."
        }

        // Status validation
        if (status.lowercase() !in listOf("single", "married", "divorced")) {
            return "Please enter status as 'Single', 'Married', or 'Divorced'."
        }

        // Email validation
        if (!email.endsWith("@gmail.com", ignoreCase = true)) {
            return "Email must end with '@gmail.com'."
        }

        // Contact number validation
        if (contactNumber.length != 11 || !contactNumber.all { it.isDigit() }) {
            return "Please enter a valid 11-digit contact number."
        }

        // Validate birthday format
        if (!isValidMonth(month)) {
            return "Please enter a valid month (e.g., January)."
        }

        if (!day.all { it.isDigit() }) {
            return "Please enter a valid day."
        }

        if (!year.all { it.isDigit() }) {
            return "Please enter a valid year."
        }

        // Ensure month, day, and year are combined correctly
        return null
    }

    // Function to check if the month is valid
    private fun isValidMonth(month: String): Boolean {
        val validMonths = listOf("january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december")
        return validMonths.contains(month.lowercase())
    }

    companion object {
        fun newInstance(): EditProfileDialogFragment {
            return EditProfileDialogFragment()
        }
    }
}
