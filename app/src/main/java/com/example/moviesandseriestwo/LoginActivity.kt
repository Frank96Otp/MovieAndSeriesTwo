package com.example.moviesandseriestwo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import com.example.moviesandseriestwo.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var fireAuth: FirebaseAuth
    private lateinit var googleLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setView()

        fireAuth = Firebase.auth

        googleLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

                Log.i("It", it.resultCode.toString())
                if (it.resultCode == RESULT_OK) {

                    val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                    try {
                        val account = task.getResult(ApiException::class.java)
                        authFireBaseWithGoogle(account.idToken)
                    } catch (_: Exception) {

                    }
                } else {
                    Toast.makeText(this, " es ${it.resultCode} ", Toast.LENGTH_LONG).show()
                }
            }

        setContentView(binding.root)
    }


    private fun setView() {

        binding.tilLogin.editText?.addTextChangedListener {
            binding.btnLogin.isEnabled =
                validateEmailPassword(it.toString(), binding.tilPassword.editText?.text.toString())
        }

        binding.tilPassword.editText?.addTextChangedListener {
            binding.btnLogin.isEnabled =
                validateEmailPassword(binding.tilLogin.editText?.text.toString(), it.toString())
        }

        binding.btnLogin.setOnClickListener {

        }

        binding.btnLoginWithGoogle.setOnClickListener {
            signinWithGoogle()
        }

        binding.btnSingUp.setOnClickListener {
            singUpWithFirebase()
        }

    }


    private fun signinWithGoogle() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_key_google))
            .requestEmail()
            .build()
        val googleClient = GoogleSignIn.getClient(this, googleSignInOptions)

        val intent = googleClient.signInIntent

        googleLauncher.launch(intent)
    }


    private fun authFireBaseWithGoogle(idToken: String?) {

        val authCredential = GoogleAuthProvider.getCredential(idToken!!, null)
        fireAuth.signInWithCredential(authCredential).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = fireAuth.currentUser
                goToMainActivity()
            } else {
                Toast.makeText(this, "OCURRIO UN ERROR", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun singUpWithFirebase() {
        TODO("Not yet implemented")
    }

    private fun validateEmailPassword(email: String, password: String): Boolean {
        val validateEmail: Boolean =
            email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val validatePassowod = password.length >= 8
        return validateEmail && validatePassowod
    }

    fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun goToSingUp(){
        val intent = Intent(this,SingUpActivity::class.java)
        startActivity(intent)
    }

}