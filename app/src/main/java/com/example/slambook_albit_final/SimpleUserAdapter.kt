package com.example.slambook_albit_final

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.slambook_albit_final.databinding.ItemUserBinding

class SimpleUserAdapter(
    private var userList: MutableList<UserData>,  // The full list of users
    private var filteredUserList: MutableList<UserData> = userList,  // The filtered list
    private val onAdminClick: (UserData) -> Unit
) : RecyclerView.Adapter<SimpleUserAdapter.UserViewHolder>() {

    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Bind data to views
        fun bind(user: UserData) {
            binding.fullName.text = user.fullName
            binding.nickName.text = user.nickName

            // Set onClickListener to trigger onAdminClick in ViewListActivity
            binding.root.setOnClickListener {
                onAdminClick(user)  // Call the callback when the item is clicked
            }
        }
    }

    // Create a new ViewHolder for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    // Bind data to the ViewHolder at the given position
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(filteredUserList[position])
    }

    // Return the total number of items in the list
    override fun getItemCount(): Int = filteredUserList.size

    // Function to update the list in the adapter
    fun updateList(newList: MutableList<UserData>) {
        userList = newList
        filteredUserList = newList // Update the filtered list as well
        notifyDataSetChanged()  // Notify the adapter that the data has changed
    }

    // Filter the list based on the search query
    fun filterList(query: String) {
        filteredUserList = if (query.isEmpty()) {
            userList // No filter applied, show full list
        } else {
            userList.filter { it.nickName.contains(query, ignoreCase = true) }.toMutableList() // Filter by nickname
        }
        notifyDataSetChanged()  // Notify the adapter that the list has changed
    }
}

