package com.marialazar.petadoption.auth

import android.util.Log
import androidx.lifecycle.*
import com.marialazar.petadoption.auth.data.AuthRepository
import com.marialazar.petadoption.auth.data.TokenHolder
import com.marialazar.petadoption.core.Result
import com.marialazar.petadoption.core.TAG
import com.marialazar.petadoption.pet.data.PetRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val petRepository: PetRepository
) : ViewModel() {

    private val mutableLoginResult = MutableLiveData<Result<TokenHolder>>()
    val loginResult: LiveData<Result<TokenHolder>> = mutableLoginResult

    val token = authRepository.token

    fun login(username: String, password: String) {
        viewModelScope.launch {
            Log.v(TAG, "login...");
            mutableLoginResult.value = authRepository.login(username, password)
        }
    }

    fun startListen() {
        petRepository.startListen()
    }
}

class AuthViewModelFactory(
    private val repository: AuthRepository,
    private val petRepository: PetRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository, petRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
