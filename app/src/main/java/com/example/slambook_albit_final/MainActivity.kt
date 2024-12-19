package com.example.slambook_albit_final

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.slambook_albit_final.Step1Activity.Companion.userDataList
import com.example.slambook_albit_final.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ImageView click animation
        val imageView: ImageView = findViewById(R.id.imageView)

        imageView.setOnClickListener {
            // Create rotation animation (360 degrees)
            val rotate = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f)

            // Create scale animation (scaling up and then back down)
            val scaleX = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 1.5f, 1f)
            val scaleY = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 1.5f, 1f)

            // Combine animations into an AnimatorSet
            val animatorSet = AnimatorSet()
            animatorSet.playTogether(rotate, scaleX, scaleY)
            animatorSet.duration = 1000 // Duration of the animation

            // Start the combined animation
            animatorSet.start()


        }


        // Button click to navigate
        binding.createSlambookButton.setOnClickListener{
            showExistingAccountDialog()
        }

        binding.viewUserButton.setOnClickListener {
            startActivity(Intent(this, ViewListActivity::class.java))
            finish()
        }
    }

    private fun showExistingAccountDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Already have existing account?")

        dialog.setPositiveButton("Yes") { _, _ ->
            // If "No" is clicked, go to CompletedActivity and load user data based on nickname
            showNickNameInputDialog()
        }

        dialog.setNegativeButton("No") { _, _ ->
            // If "Yes" is clicked, go to Step1Activity for user to fill in details
            startActivity(Intent(this, Step1Activity::class.java))
            finish()
        }

        dialog.setCancelable(false)
        dialog.show()
    }

    private fun showNickNameInputDialog() {
        val input = android.widget.EditText(this)
        input.hint = "Please enter existing nickname"

        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Login using your nickname")
        dialog.setView(input)

        dialog.setPositiveButton("OK") { _, _ ->
            val nickName = input.text.toString()
            if (nickName.isNotEmpty()) {
                // Search for the user in the userdataList
                val user = Step1Activity.userDataList.find { it.nickName == nickName }

                if (user != null) {
                    // If the user is found, pass their details to the next activity (CompletedActivity)
                    Toast.makeText(this, "User found: ${user.nickName}", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, CompletedActivity::class.java)
                    // Pass the entire user data to the next activity
                    intent.putExtra("nickName", user.nickName)
                    intent.putExtra("fullName", user.fullName)
                    intent.putExtra("email", user.email)
                    intent.putExtra("contact", user.contact)
                    intent.putExtra("address", user.address)
                    intent.putExtra("birthMonth", user.birthMonth)
                    intent.putExtra("birthDay", user.birthDay)
                    intent.putExtra("birthYear", user.birthYear)
                    intent.putExtra("status", user.status)
                    intent.putExtra("gender", user.gender)
                    startActivity(intent)
                    finish()
                } else {
                    // If no user found, show an error
                    Toast.makeText(this, "User not found.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter a nickname", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }



}
