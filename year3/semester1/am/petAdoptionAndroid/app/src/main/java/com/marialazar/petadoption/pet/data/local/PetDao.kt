package com.marialazar.petadoption.pet.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.marialazar.petadoption.pet.data.Pet

@Dao
interface PetDao {
    @Query("SELECT * from pet ORDER BY lastModified DESC")
    fun getAll(): LiveData<List<Pet>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pet: Pet)
//
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(pet: Pet)
//
    @Query("DELETE FROM pet")
    suspend fun deleteAll()
}