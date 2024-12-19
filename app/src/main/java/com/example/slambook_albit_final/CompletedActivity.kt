package com.example.slambook_albit_final

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.slambook_albit_final.databinding.ActivityCompletedBinding

class CompletedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompletedBinding

    private val favMoviesList = mutableListOf<String>()
    private  val favSongsList = mutableListOf<String>()
    private val favHobbiesList = mutableListOf<String>()
    private val favSkillsList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCompletedBinding.inflate(layoutInflater)
        setContentView(binding.root)


        handleUserData()
        setupListeners()


        binding.plusIcon.setOnClickListener {
            val movie = binding.Movies.text.toString()
            if (movie.isNotEmpty()) {
                favMoviesList.add(movie)
                binding.Movies.text.clear()
                Toast.makeText(this, "Added to Movies: $movie", Toast.LENGTH_SHORT).show()
            }
        }

        binding.plusIcon1.setOnClickListener {
            val song = binding.Songs.text.toString()
            if (song.isNotEmpty()) {
                favSongsList.add(song)
                binding.Songs.text.clear()
                Toast.makeText(this, "Added to Songs: $song", Toast.LENGTH_SHORT).show()
            }
        }

        binding.plusIcon2.setOnClickListener {
            val hobby = binding.Hobbies.text.toString()
            if (hobby.isNotEmpty()) {
                favHobbiesList.add(hobby)
                binding.Hobbies.text.clear()
                Toast.makeText(this, "Added to Hobbies: $hobby", Toast.LENGTH_SHORT).show()
            }
        }

        binding.plusIcon3.setOnClickListener {
            val skill = binding.Skills.text.toString()
            if (skill.isNotEmpty()) {
                favSkillsList.add(skill)
                binding.Skills.text.clear()
                Toast.makeText(this, "Added to Skills: $skill", Toast.LENGTH_SHORT).show()
            }
        }

        // Use the same dialog for displaying lists
        binding.eyeIcon.setOnClickListener { showDialog("Favorite Movies", favMoviesList) }
        binding.eyeIcon1.setOnClickListener { showDialog("Favorite Songs", favSongsList) }
        binding.eyeIcon2.setOnClickListener { showDialog("Favorite Hobbies", favHobbiesList) }
        binding.eyeIcon3.setOnClickListener { showDialog("Favorite Skills", favSkillsList) }

        binding.delete.setOnClickListener{

            if(favMoviesList.isEmpty()){
                Toast.makeText(this, "Movie List empty.", Toast.LENGTH_SHORT).show()
            }else {
                // Create the dialog
                AlertDialog.Builder(this)
                    .setMessage("Do you want to clear Movie List?")
                    .setPositiveButton("Yes") { _, _ ->
                        favMoviesList.clear()
                    }
                    .setNegativeButton("No", null) // Do nothing if "No" is clicked
                    .show()
            }
        }

        binding.delete1.setOnClickListener{

            if(favSongsList.isEmpty()){
                Toast.makeText(this, "Song List empty.", Toast.LENGTH_SHORT).show()
            }else {
                // Create the dialog
                AlertDialog.Builder(this)
                    .setMessage("Do you want to clear Movie List?")
                    .setPositiveButton("Yes") { _, _ ->
                        favSongsList.clear()
                    }
                    .setNegativeButton("No", null) // Do nothing if "No" is clicked
                    .show()
            }
        }

        binding.delete2.setOnClickListener{

            if(favHobbiesList.isEmpty()){
                Toast.makeText(this, "Hobby List empty.", Toast.LENGTH_SHORT).show()
            }else {
                // Create the dialog
                AlertDialog.Builder(this)
                    .setMessage("Do you want to clear Movie List?")
                    .setPositiveButton("Yes") { _, _ ->
                        favHobbiesList.clear()
                    }
                    .setNegativeButton("No", null) // Do nothing if "No" is clicked
                    .show()
            }
        }

        binding.delete3.setOnClickListener{

            if(favSkillsList.isEmpty()){
                Toast.makeText(this, "Skill List empty.", Toast.LENGTH_SHORT).show()
            }else {
                // Create the dialog
                AlertDialog.Builder(this)
                    .setMessage("Do you want to clear Movie List?")
                    .setPositiveButton("Yes") { _, _ ->
                        favSkillsList.clear()
                    }
                    .setNegativeButton("No", null) // Do nothing if "No" is clicked
                    .show()
            }
        }

        val nickname = intent.getStringExtra("nickname")

        if (nickname != null) {
            // Fetch user data based on the nickname (e.g., from SharedPreferences)
            val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
            val fullName = sharedPreferences.getString("fullName", "Unknown")

        }

        binding.logout.setOnClickListener {
            // Show confirmation dialog
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to log out?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    // Store the current user's nickname before logging out
                    val nickname = Step1Activity.userDataList.firstOrNull()?.nickName ?: ""
                    val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("nickname", nickname) // Save the nickname
                    editor.apply()

                    // Start MainActivity
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                .setNegativeButton("No") { dialog, id -> dialog.dismiss() }
            val alert = builder.create()
            alert.show()
        }


        // Open EditProfileDialogFragment when clicking the edit icon
        binding.Info.setOnClickListener {
                val dialog = InfoDialogFragment.newInstance()
                dialog.show(supportFragmentManager, "InfoDialogFragment")

        }

    }

    private fun handleUserData() {
        // Get the nickname passed from the previous activity (MainActivity or after logout)
        val nickName = intent.getStringExtra("nickName")

        if (nickName != null) {
            // Find the user data based on the nickname
            val userData = Step1Activity.userDataList.find { it.nickName == nickName }

            if (userData != null) {
                // Set the text views with the user data
                binding.fullNameText.text = "Full Name: ${userData.fullName}"
                binding.nickNameText.text = "Nickname: ${userData.nickName}"
                binding.genderText.text = "Gender: ${userData.gender}"
                binding.birthdayText.text = "Birthday: ${userData.birthMonth} ${userData.birthDay}, ${userData.birthYear}"
                binding.statusText.text = "Status: ${userData.status}"
                binding.emailText.text = "Email: ${userData.email}"
                binding.contactText.text = "Contact Number: ${userData.contact}"
                binding.addressText.text = "Address: ${userData.address}"
            } else {
                // Handle case where no user data exists for the given nickname
                Log.e("CompletedActivity", "No user data found for nickname: $nickName")
            }
        }
    }



    private fun setupListeners() {

        // Open EditProfileDialogFragment when clicking the edit icon
        binding.editProfile.setOnClickListener {
            val userData = Step1Activity.userDataList.firstOrNull()

            if (userData != null) {
                // Pass data to EditProfileDialogFragment
                val dialog = EditProfileDialogFragment.newInstance()
                dialog.show(supportFragmentManager, "EditProfileDialogFragment")
            }
        }

    }

    fun onProfileUpdated(
        fullName: String, nickName: String, gender: String,
        birthday: String, status: String, email: String,
        contactNumber: String, address: String
    ) {
        // Update the user data in the list
        Step1Activity.userDataList[0].apply {
            this.fullName = fullName
            this.nickName = nickName
            this.gender = gender
            val splitBirthday = birthday.split(" ")
            if (splitBirthday.size == 3) {
                this.birthMonth = splitBirthday[0]
                this.birthDay = splitBirthday[1]
                this.birthYear = splitBirthday[2]
            }
            this.status = status
            this.email = email
            this.contact = contactNumber
            this.address = address
        }

        // Refresh user data display
        handleUserData()
    }

    private fun showDialog(title: String, items: MutableList<String>) {
        if (items.isEmpty()) {
            Toast.makeText(this, "No items added yet.", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a custom adapter to display items with numbers
        var numberedItems = items.mapIndexed { index, item -> "${index + 1}. $item" }.toMutableList()

        // Create an adapter for the numbered list items
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, numberedItems)

        // Create a ListView to show the items
        val listView = ListView(this)
        listView.adapter = adapter

        // Create the dialog
        val dialog = AlertDialog.Builder(this)
            .setTitle(title)
            .setView(listView)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()

        // Set the long-click listener for each item
        listView.setOnItemLongClickListener { parent, view, position, id ->
            val selectedItem = items[position]  // Get the item at the selected position

            // Show delete confirmation dialog
            showDeleteDialog(selectedItem, items, adapter, numberedItems)

            true // Return true to indicate the long-click event is handled
        }

        dialog.show()
    }

    private fun showDeleteDialog(
        item: String,
        items: MutableList<String>,
        adapter: ArrayAdapter<String>,
        numberedItems: MutableList<String>
    ) {
        AlertDialog.Builder(this)
            .setMessage("Are you sure you want to delete '$item' from the list?")
            .setPositiveButton("Yes") { _, _ ->
                // Remove the item from the list
                items.remove(item)

                // Update numberedItems after removal
                numberedItems.clear()
                numberedItems.addAll(items.mapIndexed { index, item -> "${index + 1}. $item" })

                // Notify the adapter that the data has changed, which updates the ListView
                adapter.notifyDataSetChanged()

                // Show a toast message after deletion
                Toast.makeText(this, "'$item' has been deleted.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null) // Do nothing if "No" is clicked
            .show()
    }


}





