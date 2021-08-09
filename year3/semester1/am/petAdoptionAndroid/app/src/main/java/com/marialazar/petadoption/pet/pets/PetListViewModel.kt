package com.marialazar.petadoption.pet.pets

import android.util.Log
import androidx.lifecycle.*
import com.marialazar.petadoption.auth.data.AuthRepository
import com.marialazar.petadoption.core.TAG
import com.marialazar.petadoption.pet.data.Pet
import com.marialazar.petadoption.pet.data.PetRepository
import kotlinx.coroutines.launch

class PetListViewModel(
    private val authRepository: AuthRepository,
    private val petRepository: PetRepository
) : ViewModel() {
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val pets: LiveData<List<Pet>> = petRepository.cachedPets
    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Exception> = mutableException

    fun loadPets() {
        viewModelScope.launch {
            Log.v(TAG, "loadPets...");
            mutableLoading.value = true
            mutableException.value = null
            try {
                petRepository.loadAll()
                Log.d(TAG, "loadPets succeeded");
                mutableLoading.value = false
            } catch (e: Exception) {
                Log.w(TAG, "loadPets failed", e);
                mutableException.value = e
                mutableLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            petRepository.stopListen()
            petRepository.clear()
        }
    }
}

class PetViewModelFactory(
    private val authRepository: AuthRepository,
    private val petRepository: PetRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PetListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PetListViewModel(authRepository, petRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}