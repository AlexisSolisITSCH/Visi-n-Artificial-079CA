package com.example.visionartificial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.inputmethod.InputBinding
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mContext : Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val botonCerrarSesion: Button = findViewById(R.id.Btn_Salir)
        firebaseAuth = Firebase.auth
        leerInfo()

        botonCerrarSesion.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@MainActivity, Inicio::class.java)
            Toast.makeText(applicationContext, "Cerrando Sesion...", Toast.LENGTH_SHORT).show()
            startActivity(intent)
            finish()
        }
    }

    private fun leerInfo() {
        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child("${firebaseAuth.uid}")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nombres = "${snapshot.child("n_usuario").value}"
                    val email = "${snapshot.child("email").value}"

                    val textViewNombres: TextView = findViewById(R.id.Tv_nombre_usuario)
                    val textViewEmail: TextView = findViewById(R.id.Tv_email)

                    textViewNombres.text = nombres
                    textViewEmail.text = email
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("MainActivity", "Error al leer datos de Firebase", error.toException())
                }
            })
    }
}