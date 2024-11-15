package com.example.visionartificial

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.ref.Reference
import java.util.HashMap

class RegistroActivity : AppCompatActivity() {

    private lateinit var R_Et_nombre_usuario : EditText
    private lateinit var R_Et_email :EditText
    private lateinit var R_Et_password : EditText
    private lateinit var R_Et_r_password : EditText
    private lateinit var Btn_registrar : Button

    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        InicializarVariables()

        Btn_registrar.setOnClickListener {
            validarDatos()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun InicializarVariables(){
        R_Et_nombre_usuario = findViewById(R.id.R_Et_nombre_usuario)
        R_Et_email = findViewById(R.id.R_Et_email)
        R_Et_password = findViewById(R.id.R_Et_password)
        R_Et_r_password = findViewById(R.id.R_Et_r_password)
        Btn_registrar = findViewById(R.id.Btn_registrar)
        auth = FirebaseAuth.getInstance()
    }

    private fun validarDatos() {
        val nombre_usuario : String = R_Et_nombre_usuario.text.toString()
        val email :  String = R_Et_email.text.toString()
        val password : String = R_Et_password.text.toString()
        val r_password : String = R_Et_r_password.text.toString()

        if (nombre_usuario.isEmpty()){
            Toast.makeText(applicationContext, "Ingrese un nombre de usuario", Toast.LENGTH_SHORT).show()
        }
        else if (email.isEmpty()){
            Toast.makeText(applicationContext, "Ingrese un correo electronico", Toast.LENGTH_SHORT).show()
        }
        else if (password.isEmpty()){
            Toast.makeText(applicationContext, "Ingrese una contraseña", Toast.LENGTH_SHORT).show()
        }
        else if (r_password.isEmpty()){
            Toast.makeText(applicationContext, "Repita la contraseña", Toast.LENGTH_SHORT).show()
        }
        else if (password != r_password){
            Toast.makeText(applicationContext, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
        }
        else{
            RegistrarUsuario(email, password)
        }
    }

    private fun RegistrarUsuario(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{task->
                if(task.isSuccessful){
                    var uid : String = ""
                    uid = auth.currentUser!!.uid
                    reference = FirebaseDatabase.getInstance().reference.child("Usuarios").child(uid)

                    val hashmap : HashMap<String, Any> = HashMap()
                    val h_nombre_usuario : String = R_Et_nombre_usuario.text.toString()
                    val h_email : String = R_Et_email.text.toString()

                    hashmap["uid"] = uid
                    hashmap["n_usuario"] = h_nombre_usuario
                    hashmap["email"] = h_email
                    hashmap["imagen"] = ""
                    hashmap["buscar"] = h_nombre_usuario.lowercase()

                    reference.updateChildren(hashmap).addOnCompleteListener { task2->
                        if(task2.isSuccessful){
                            val intent = Intent(this@RegistroActivity, MainActivity::class.java)
                            Toast.makeText(applicationContext, "Se ha registrado con exito", Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                        }
                    }
                        .addOnFailureListener { e->
                        Toast.makeText(applicationContext, "${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(applicationContext, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
                }
        }
            .addOnFailureListener { e->
                Toast.makeText(applicationContext, "${e.message}", Toast.LENGTH_SHORT).show()
            }

    }
}












