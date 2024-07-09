package com.surajpurohit.notesbuddy.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.surajpurohit.notesbuddy.R
import com.surajpurohit.notesbuddy.databinding.FragmentOnboardingBinding
import com.surajpurohit.notesbuddy.utils.AppConstant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class OnboardingFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentOnboardingBinding.inflate(inflater, container, false)

        auth = Firebase.auth

        binding.googleAuthBtn.setOnClickListener {
            val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false) // Query all google accounts on the device
                .setServerClientId(AppConstant.WEB_CLIENT_ID)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val credentialManager = CredentialManager.create(requireContext())

            GlobalScope.launch {
                try {
                    val result = credentialManager.getCredential(requireContext(), request)
                    handleSignIn(result)
                } catch (e: GetCredentialException) {
                    Log.e("GoogleAuth", "GetCredentialException", e)
                    requestSignInWithAllAccounts()
                }
            }

        }

        return binding.root
    }

    private fun requestSignInWithAllAccounts() {
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false) // Query all google accounts on the device
            .setServerClientId(AppConstant.WEB_CLIENT_ID)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val credentialManager = CredentialManager.create(requireContext())

        GlobalScope.launch {
            try {
                val result = credentialManager.getCredential(requireContext(), request)
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                Log.e("GoogleAuth", "GetCredentialException", e)
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        val credential = result.credential
        val authCredential =
            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential =
                    GoogleIdTokenCredential.createFrom(credential.data)

                GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

            } else {
                throw RuntimeException("Received an invalid credential type")
            }

        auth.signInWithCredential(authCredential)
        CoroutineScope(Dispatchers.Main).launch {
            findNavController().navigate(R.id.action_onboardingFragment_to_homeFragment)
        }

    }

}