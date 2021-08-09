package com.marialazar.petadoption.auth.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.marialazar.petadoption.auth.data.TokenHolder

@Dao
interface TokenDao {
    @Query("SELECT token FROM token_holder LIMIT 1")
    fun getToken(): LiveData<List<TokenHolder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tokenHolder: TokenHolder)

    @Query("DELETE FROM token_holder")
    suspend fun deleteAll()
}