package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignInActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignup: Button

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        supportActionBar?.hide()

        etName = findViewById(R.id.et_name)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        btnSignup = findViewById(R.id.btn_signup)

        mAuth = FirebaseAuth.getInstance()


        btnSignup.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            signUp(name ,email, password)
        }
    }

    private fun signUp(name: String,email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful){
                    Log.d("Tag", "Sign up successful.")
                    addUserToDb(name, email, mAuth.currentUser?.uid!!)
                    val intent = Intent(this@SignInActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    Log.w("Tag","Sign up failed.", task.exception)
                    Toast.makeText(this, "Sign up failed.",Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDb(name: String, email: String, uid: String) {
        mDbRef = FirebaseDatabase.getInstance().getReference()

        mDbRef.child("user").child(uid).setValue(User(name,email,uid))
    }
}