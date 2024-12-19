package com.example.slambook_albit_final

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.slambook_albit_final.databinding.ActivityStep1Binding
import java.time.Year
import java.util.*

class Step1Activity : AppCompatActivity() {

    private lateinit var binding: ActivityStep1Binding

    companion object {
        val userDataList = mutableListOf<UserData>() // Shared user data list
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStep1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set default gender to Male
        binding.genderMale.isChecked = true

        setupContactInputFilter()

        // Populate spinner with years
        val yearsArray = generateYears()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, yearsArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.birthdayYear.adapter = adapter

        // Populate spinner with months (assuming it's already set)
        val monthsArray = resources.getStringArray(R.array.months)
        val monthAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, monthsArray)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.birthdayMonth.adapter = monthAdapter

        // Listen to month selection to adjust the days spinner
        binding.birthdayMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onItemSelected(parentView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedMonth = position
                val selectedYear = binding.birthdayYear.selectedItem.toString().toInt() // Get selected year
                updateDaysSpinner(selectedMonth, selectedYear)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing if no month is selected
            }
        }

        binding.backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.saveButton.setOnClickListener {
            saveUserData()
        }
    }

    private fun loadUserData(userData: UserData) {
        binding.fullName.setText(userData.fullName)
        binding.nickName.setText(userData.nickName)
        when (userData.gender) {
            "Male" -> binding.genderMale.isChecked = true
            "Female" -> binding.genderFemale.isChecked = true
        }
        binding.email.setText(userData.email)
        binding.contact.setText(userData.contact)
        binding.address.setText(userData.address)
        binding.birthdayMonth.setSelection(getPositionOfMonth(userData.birthMonth))
        binding.birthdayDay.setSelection(getPositionOfDay(userData.birthDay))
        binding.birthdayYear.setSelection(getPositionOfYear(userData.birthYear))
        binding.status.setSelection(getPositionOfStatus(userData.status))
    }

    private fun saveUserData() {
        val nickName = binding.nickName.text.toString()
        val fullName = binding.fullName.text.toString()
        val email = binding.email.text.toString()
        val contact = binding.contact.text.toString()
        val address = binding.address.text.toString()
        val birthMonth = binding.birthdayMonth.selectedItem.toString()
        val birthDay = binding.birthdayDay.selectedItem.toString()
        val birthYear = binding.birthdayYear.selectedItem.toString()
        val status = binding.status.selectedItem.toString()
        val gender = if (binding.genderMale.isChecked) "Male" else "Female"

        // Check if the nickname is already used
        if (userDataList.any { it.nickName == nickName }) {
            Toast.makeText(this, "Nickname already exists. Please choose a different one.", Toast.LENGTH_SHORT).show()
            return
        }

        // Input validation (kept as in your code)
        if (fullName.isEmpty() || email.isEmpty() || contact.isEmpty() || address.isEmpty() || nickName.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show()
            return
        }


        if (!email.endsWith("@gmail.com")) {
            Toast.makeText(this, "Please use a Gmail address.", Toast.LENGTH_SHORT).show()
            return
        }


        val contactEditText = findViewById<EditText>(R.id.contact)  // Reference your EditText correctly
        val contact1 = contactEditText.text.toString().trim() // Get input and remove leading/trailing spaces

// Validate the phone number format
        val transformedContact = when {
            // If it starts with 09 and has exactly 11 digits, it's valid
            contact1.startsWith("09") && contact1.length == 11 -> contact1
            else -> null // Invalid input (either incorrect prefix or length)
        }

// Validate the phone number length and format
        if (transformedContact != null && transformedContact.matches(Regex("^09\\d{9}$"))) {
            // Valid number
            Toast.makeText(this, "Valid contact: $transformedContact", Toast.LENGTH_SHORT).show()
            // Proceed to save the transformedContact or pass it to the next activity
        } else {
            // Invalid number
            Toast.makeText(this, "Contact number must be 11 digits, starting with 09.", Toast.LENGTH_SHORT).show()
            return
        }



        // Get the current date
        val currentDate = Calendar.getInstance()

        // Get the user's selected birth year, month, and day
        val birthMonthInt = binding.birthdayMonth.selectedItemPosition + 1 // months are 0-indexed, so we add 1
        val birthDayInt = binding.birthdayDay.selectedItem.toString().toInt()
        val birthYearInt = binding.birthdayYear.selectedItem.toString().toInt()

        // Calculate the user's age
        var age = currentDate.get(Calendar.YEAR) - birthYearInt

        // Check if the user has already had their birthday this year
        val hasHadBirthdayThisYear = currentDate.get(Calendar.MONTH) + 1 > birthMonthInt ||
                (currentDate.get(Calendar.MONTH) + 1 == birthMonthInt && currentDate.get(Calendar.DAY_OF_MONTH) >= birthDayInt)

        if (!hasHadBirthdayThisYear) {
            age -= 1 // Decrease age by 1 if they haven't had their birthday yet this year
        }

        // Check if the age is below 5 years
        if (age < 5) {
            Toast.makeText(this, "You must be at least 5 years old.", Toast.LENGTH_SHORT).show()
            return
        }

        // Save data (don't clear the list)
        val userData = UserData(
            nickName, fullName, gender, email, transformedContact, address, birthMonth, birthDay, birthYear, status
        )
        userDataList.add(userData) // Add new data without clearing the list

        Toast.makeText(this, "Welcome $fullName!", Toast.LENGTH_SHORT).show()

        // Open CompletedActivity after saving user data
        val intent = Intent(this, CompletedActivity::class.java)
        intent.putExtra("nickName", userData.nickName)
        intent.putExtra("fullName", userData.fullName)
        intent.putExtra("email", userData.email)
        intent.putExtra("contact", userData.contact)
        intent.putExtra("address", userData.address)
        intent.putExtra("birthMonth", userData.birthMonth)
        intent.putExtra("birthDay", userData.birthDay)
        intent.putExtra("birthYear", userData.birthYear)
        intent.putExtra("status", userData.status)
        intent.putExtra("gender", userData.gender)
        startActivity(intent)
        finish()
    }


    private fun setupContactInputFilter() {
        binding.contact.filters = arrayOf(
            InputFilter.LengthFilter(11),
            InputFilter { source, start, end, dest, dstart, dend ->
                source.filter { it.isDigit() }.take(11 - dest.length).toString()
            }
        )
    }

    private fun getPositionOfMonth(month: String?) = resources.getStringArray(R.array.months).indexOf(month ?: "")
    private fun getPositionOfDay(day: String?) = resources.getStringArray(R.array.days).indexOf(day ?: "")
    private fun getPositionOfYear(year: String?) = generateYears().indexOf(year ?: "")
    private fun getPositionOfStatus(status: String?) = resources.getStringArray(R.array.status).indexOf(status ?: "")

    private fun generateYears(): Array<String> {
        val currentYear = 2024
        return (1900..currentYear).toList().reversed().map { it.toString() }.toTypedArray()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateDaysSpinner(month: Int, year: Int) {
        val daysList: List<Int> = if (year == 2024 && month == 11) {
            // If it's December 2024, limit days to 1-15
            (1..15).toList()
        } else {
            // Otherwise, get the correct number of days for the selected month and year
            val daysInMonth = getDaysInMonth(month, year)
            (1..daysInMonth).toList()
        }

        val dayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, daysList)
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.birthdayDay.adapter = dayAdapter
    }


    // Function to return the number of days in a month (handles leap years for February)
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDaysInMonth(month: Int, year: Int): Int {
        return when (month) {
            0 -> 31 // January
            1 -> if (Year.isLeap(year.toLong())) 29 else 28 // February (leap year handling)
            2 -> 31 // March
            3 -> 30 // April
            4 -> 31 // May
            5 -> 30 // June
            6 -> 31 // July
            7 -> 31 // August
            8 -> 30 // September
            9 -> 31 // October
            10 -> 30 // November
            11 -> 31 // December
            else -> 31 // Default case
        }
    }
}
