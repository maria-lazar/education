package com.marialazar.petadoption.pet.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.marialazar.petadoption.RemoteDataSource
import com.marialazar.petadoption.auth.data.local.TokenDao
import com.marialazar.petadoption.core.TAG
import com.marialazar.petadoption.pet.data.local.PetDao
import com.marialazar.petadoption.pet.data.remote.PetApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class EventData(val event: String, val payload: Pet)

class PetRepository(private val petDao: PetDao) {
    val cachedPets: LiveData<List<Pet>> = petDao.getAll()
    private var isActive = false;

    suspend fun loadAll() {
        Log.i(TAG, "loadAll")
        val pets = PetApi.service.find()
        pets.forEach { pet ->
            val index = cachedPets.value?.indexOfFirst { it._id == pet._id }
            if (index == -1) {
                petDao.insert(pet)
            } else {
                petDao.update(pet)
            }
        }
    }

    suspend fun load(petId: String): Pet {
        Log.i(TAG, "load")
        val pet = cachedPets.value?.find { it._id == petId }
        if (pet != null) {
            return pet
        }
        return PetApi.service.read(petId)
    }


    suspend fun save(pet: Pet): Pet {
        Log.i(TAG, "save")
        val createdPet = PetApi.service.create(pet)
        val index = cachedPets.value?.indexOfFirst { it._id == createdPet._id }
        if (index != null && index != -1) {
            petDao.update(createdPet)
        } else {
            petDao.insert(createdPet)
        }
        return createdPet
    }

    suspend fun update(pet: Pet): Pet {
        Log.i(TAG, "update")
        val updatedPet = PetApi.service.update(pet._id, pet)
        val index = cachedPets.value?.indexOfFirst { it._id == pet._id }
        if (index != null && index != -1) {
            petDao.update(updatedPet)
        }
        return updatedPet
    }

    private suspend fun collectEvents() {
        Log.d("collectEvents", "start collecting events")
        while (isActive) {
            val event = RemoteDataSource.eventChannel.receive()
            val eventObject = Gson().fromJson(event, EventData::class.java)
            val pet = eventObject.payload
            if (eventObject.event == "updated" || eventObject.event == "created") {
                val index = cachedPets.value?.indexOfFirst { it._id == pet._id }
                if (index != null && index != -1) {
                    petDao.update(pet)
                } else {
                    petDao.insert(pet)
                }
            }
            Log.d("PetRepository", "received $eventObject")
        }
    }

    fun startListen() {
        RemoteDataSource.createWebSocket()
        isActive = true
        CoroutineScope(Dispatchers.Default).launch { collectEvents() }
    }

    fun stopListen() {
        Log.i(TAG, "stop listen")
        isActive = false;
        RemoteDataSource.destroyWebSocket()
    }

    suspend fun clear() {
        petDao.deleteAll()
    }
}