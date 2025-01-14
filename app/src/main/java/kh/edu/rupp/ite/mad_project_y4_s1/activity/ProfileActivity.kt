package kh.edu.rupp.ite.mad_project_y4_s1.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import kh.edu.rupp.ite.mad_project_y4_s1.R
import kh.edu.rupp.ite.mad_project_y4_s1.viewmodel.UserViewModel
import kh.edu.rupp.ite.mad_project_y4_s1.model.ApiState
import kh.edu.rupp.ite.mad_project_y4_s1.model.User
import com.google.firebase.auth.FirebaseAuth
import kh.edu.rupp.ite.mad_project_y4_s1.model.PaymentMethod
import com.google.firebase.auth.EmailAuthProvider
import android.view.View

class ProfileActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()
    private val auth = FirebaseAuth.getInstance()

    private lateinit var firstNameEdit: EditText
    private lateinit var lastNameEdit: EditText
    private lateinit var addressEdit: EditText
    private lateinit var cityEdit: EditText
    private lateinit var phoneNumberEdit: EditText
    private lateinit var saveButton: Button
    private lateinit var cardNumberEdit: EditText
    private lateinit var cvvEdit: EditText
    private lateinit var expMonthEdit: EditText
    private lateinit var expYearEdit: EditText
    private lateinit var nameOnCardEdit: EditText
    private lateinit var currentPasswordEdit: EditText
    private lateinit var newPasswordEdit: EditText
    private lateinit var confirmNewPasswordEdit: EditText
    private lateinit var updatePasswordButton: Button
    private lateinit var loginLogoutButton: Button
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        // Set the selected item to the current activity
        bottomNavigationView.selectedItemId = R.id.nav_profile
        // Set up the BottomNavigationView listener
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Navigate to HomeActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.shoppingButton -> {
                    // Navigate to ShopActivity
                    val intent = Intent(this, ItemsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.order -> {
                    startActivity(Intent(this, OrderActivity::class.java))
                    true
                }
                R.id.nav_blog -> {
                    val intent = Intent(this, BlogActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    // Navigate to ProfileActivity
                    true
                }
                else -> false
            }

        }
        // Handle the back button click
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val origin = intent.getStringExtra("origin") // Retrieve the origin
            when (origin) {
                "BlogDetailActivity" -> {
                    val intent = Intent(this, BlogDetailActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }
                "BlogActivity" -> {
                    val intent = Intent(this, BlogActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }
                "MainActivity" -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }
                else -> {
                    finish() // Default behavior if no origin is specified
                }
            }

        }

        // Initialize views
        firstNameEdit = findViewById(R.id.firstNameEdit)
        lastNameEdit = findViewById(R.id.lastNameEdit)
        addressEdit = findViewById(R.id.addressEdit)
        cityEdit = findViewById(R.id.cityEdit)
        phoneNumberEdit = findViewById(R.id.phoneNumberEdit)
        cardNumberEdit = findViewById(R.id.cardNumberEdit)
        cvvEdit = findViewById(R.id.cvvEdit)
        expMonthEdit = findViewById(R.id.expMonthEdit)
        expYearEdit = findViewById(R.id.expYearEdit)
        nameOnCardEdit = findViewById(R.id.nameOnCardEdit)
        saveButton = findViewById(R.id.saveButton)
        currentPasswordEdit = findViewById(R.id.currentPasswordEdit)
        newPasswordEdit = findViewById(R.id.newPasswordEdit)
        confirmNewPasswordEdit = findViewById(R.id.confirmNewPasswordEdit)
        updatePasswordButton = findViewById(R.id.updatePasswordButton)
        loginLogoutButton = findViewById(R.id.loginLogoutButton)

        // Setup login/logout button
        updateLoginLogoutButton()

        // Get current user data
        auth.currentUser?.let { firebaseUser ->
            lifecycleScope.launch {
                userViewModel.getCurrentUser(firebaseUser.uid)
            }
        }

        // Observe user data changes
        lifecycleScope.launch {
            userViewModel.userState.collect { response ->
                when (response.status) {
                    ApiState.LOADING -> {
                        // Show loading indicator if needed
                    }
                    ApiState.SUCCESS -> {
                        response.data?.let { user ->
                            // Populate fields with user data
                            firstNameEdit.setText(user.firstName)
                            lastNameEdit.setText(user.lastName)
                            addressEdit.setText(user.address)
                            cityEdit.setText(user.city)
                            phoneNumberEdit.setText(user.phoneNumber)

                            // Populate payment fields
                            user.paymentMethod?.let { payment ->
                                cardNumberEdit.setText(payment.cardNumber)
                                cvvEdit.setText(payment.cvv)
                                expMonthEdit.setText(payment.expMonth)
                                expYearEdit.setText(payment.expYear)
                                nameOnCardEdit.setText(payment.nameOnCard)
                            }
                        }
                    }
                    ApiState.ERROR -> {
                        Toast.makeText(this@ProfileActivity, 
                            response.error ?: "Error loading profile", 
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Handle save button click
        saveButton.setOnClickListener {
            auth.currentUser?.let { firebaseUser ->
                val paymentMethod = PaymentMethod(
                    cardNumber = cardNumberEdit.text.toString(),
                    cvv = cvvEdit.text.toString(),
                    expMonth = expMonthEdit.text.toString(),
                    expYear = expYearEdit.text.toString(),
                    nameOnCard = nameOnCardEdit.text.toString()
                )

                val updatedUser = User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    firstName = firstNameEdit.text.toString(),
                    lastName = lastNameEdit.text.toString(),
                    address = addressEdit.text.toString(),
                    city = cityEdit.text.toString(),
                    phoneNumber = phoneNumberEdit.text.toString(),
                    paymentMethod = paymentMethod
                )
                lifecycleScope.launch {
                    userViewModel.updateUser(updatedUser)
                }
            }
        }

        // Handle password update
        updatePasswordButton.setOnClickListener {
            val currentPassword = currentPasswordEdit.text.toString()
            val newPassword = newPasswordEdit.text.toString()
            val confirmNewPassword = confirmNewPasswordEdit.text.toString()

            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all password fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != confirmNewPassword) {
                Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Minimum password length check
            if (newPassword.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Re-authenticate user before changing password
            auth.currentUser?.let { user ->
                val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)
                
                user.reauthenticate(credential)
                    .addOnSuccessListener {
                        // Update password
                        user.updatePassword(newPassword)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show()
                            }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Invalid current password", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun updateLoginLogoutButton() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is logged in
            loginLogoutButton.text = "Log Out"
            loginLogoutButton.setOnClickListener {
                auth.signOut()
                Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
                // Redirect to login screen
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        } else {
            // User is not logged in
            loginLogoutButton.text = "Log In"
            loginLogoutButton.setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateLoginLogoutButton()
    }
    fun onSearchButtonClick(view: View) {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }
} 