package com.entsh118.housespot.ui.auth.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.entsh118.housespot.BuildConfig
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class OnboardingViewModel(application: Application) : AndroidViewModel(application) {

    private val googleSignInClient: GoogleSignInClient
    val signInResult = MutableLiveData<GoogleSignInAccount?>()
    val signInError = MutableLiveData<Exception?>()

    init {
        googleSignInClient = GoogleSignIn.getClient(application, getGoogleSignInOptions())
    }

    private fun getGoogleSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.DEFAULT_WEB_CLIENT_ID)
            .requestEmail()
            .build()
    }

    fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        signInIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        getApplication<Application>().startActivity(signInIntent)
    }

    fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            signInResult.postValue(account)
        } catch (e: ApiException) {
            signInError.postValue(e)
        }
    }
}
