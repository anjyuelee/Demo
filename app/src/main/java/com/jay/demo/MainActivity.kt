package com.jay.demo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.database.*
import com.jay.demo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseApp.initializeApp(/*context=*/this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            SafetyNetAppCheckProviderFactory.getInstance()
        )

        initView()

        val addA = 0

        val addB = 1
    }

    private fun initView() {
        binding.btnInsertToFirebase.setOnClickListener {
            insertMessageToFireBaseDatabase(
                binding.edInsertKey.text.toString(),
                binding.edInsertValue.text.toString()
            )
        }

        binding.btnReadFromFirebase.setOnClickListener {
            readMessageFromFireBaseDatabase(binding.edReadKey.text.toString())
        }
    }

    private fun insertMessageToFireBaseDatabase(key: String, value: String) {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val ref: DatabaseReference = database.getReference(key)

        ref.setValue(value)
    }

    private fun readMessageFromFireBaseDatabase(key: String) {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val ref: DatabaseReference = database.getReference(key)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.value
                binding.tvMessage.text = value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(MainActivity::class.java.simpleName, error.message)
            }

        })
    }
}