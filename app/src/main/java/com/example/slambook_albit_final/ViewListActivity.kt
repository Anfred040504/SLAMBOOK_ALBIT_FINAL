package com.example.slambook_albit_final

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.slambook_albit_final.databinding.ActivityViewListBinding

class ViewListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewListBinding
    private lateinit var userAdapter: SimpleUserAdapter
    private val userList = mutableListOf<UserData>() // Store all users
    private val userListFiltered = mutableListOf<UserData>() // Store filtered users

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize with existing data from Step1Activity
        userList.addAll(Step1Activity.userDataList)
        userListFiltered.addAll(userList)

        setupRecyclerView()

        binding.backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Set up the search functionality
        binding.searchNickName.addTextChangedListener { text ->
            // Filter the list whenever the text changes
            userAdapter.filterList(text.toString())
        }
    }

    private fun setupRecyclerView() {
        userAdapter = SimpleUserAdapter(userList, userListFiltered) { user ->
            showDeleteConfirmationDialog(user)
            // Handle the item click if needed
        }
        binding.userRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ViewListActivity)
            adapter = userAdapter
            setHasFixedSize(true)
        }
    }

    private fun showDeleteConfirmationDialog(user: UserData) {
        // Create and display the confirmation dialog
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete ${user.nickName}?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                // Call the function to delete the user
                deleteUser(user)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()  // Dismiss the dialog if "No" is clicked
            }

        builder.create().show()  // Show the dialog
    }

    private fun deleteUser(user: UserData) {
        // Remove the user from the list and notify the adapter
        userList.remove(user)
        userListFiltered.remove(user)

        // Update the static list in Step1Activity
        Step1Activity.userDataList.remove(user)  // This will update the list in Step1Activity

        // Update the RecyclerView with the new list
        userAdapter.updateList(userList)
    }


    // Function to add a new user and update the list
    private fun addUserToList(user: UserData) {
        userList.add(user)  // Add the new user
        userListFiltered.add(user) // Add to the filtered list too
        userAdapter.updateList(userList)  // Notify the adapter to update the RecyclerView
    }
}

