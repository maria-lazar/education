package com.marialazar.petadoption.pet.pet

import android.util.Log
import androidx.lifecycle.*
import com.marialazar.petadoption.core.TAG
import com.marialazar.petadoption.pet.data.Pet
import com.marialazar.petadoption.pet.data.PetRepository
import kotlinx.coroutines.launch
import java.util.*

class PetEditViewModel(private val petRepository: PetRepository) : ViewModel() {
    private val mutablePet = MutableLiveData<Pet>().apply {
        value = Pet(
            "", "", "", "", "", false,
            0F, Date().time, "", "", lastModified = 0, version = 0
        )
    }
    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val pet: LiveData<Pet> = mutablePet
    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    fun loadPet(petId: String) {
        viewModelScope.launch {
            Log.i(TAG, "loadPet...")
            mutableFetching.value = true
            mutableException.value = null
            try {
                mutablePet.value = petRepository.load(petId)
                Log.i(TAG, "loadPet succeeded")
                mutableFetching.value = false
            } catch (e: Exception) {
                Log.w(TAG, "loadPet failed", e)
                mutableException.value = e
                mutableFetching.value = false
            }
        }
    }

    fun saveOrUpdatePet(
        name: String,
        description: String,
        birthDate: String,
        weight: String,
        breed: String,
        ownerName: String,
        type: String,
        vaccinated: String
    ) {
        viewModelScope.launch {
            try {
                Log.i(TAG, "saveOrUpdatePet...");
                val pet = mutablePet.value ?: return@launch
                pet.name = name;
                pet.description = description;
                pet.birthDate = Date(birthDate).time;
                pet.weight = weight.toFloat();
                pet.ownerName = ownerName;
                pet.breed = breed;
                pet.type = type;
                pet.vaccinated = vaccinated == "true"
                mutableFetching.value = true
                mutableException.value = null
                if (name == "" || description == "" || birthDate == "" || weight == "" || breed == "" || type == "" || vaccinated == "") {
                    Log.w(TAG, "saveOrUpdatePet failed");
                    mutableException.value = Exception("All fields must be entered")
                    mutableFetching.value = false
                    return@launch
                }
                pet.lastModified = System.currentTimeMillis();
                if (pet._id.isNotEmpty()) {
                    pet.owner = mutablePet.value!!.owner;
                    pet.version = pet.version;
                    mutablePet.value = petRepository.update(pet)
                } else {
                    mutablePet.value = petRepository.save(pet)
                }
                Log.i(TAG, "saveOrUpdatePet succeeded");
                mutableCompleted.value = true
                mutableFetching.value = false
            } catch (e: Exception) {
                Log.w(TAG, "saveOrUpdatePet failed", e);
                mutableException.value = e
                mutableFetching.value = false
            }
        }
    }
}

class PetEditViewModelFactory(
    private val petRepository: PetRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PetEditViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PetEditViewModel(petRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
