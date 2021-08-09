package com.marialazar.petadoption

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.marialazar.petadoption.auth.data.TokenHolder
import com.marialazar.petadoption.auth.data.local.TokenDao
import com.marialazar.petadoption.pet.data.Pet
import com.marialazar.petadoption.pet.data.local.PetDao
import kotlinx.coroutines.CoroutineScope

@Database(entities = [TokenHolder::class, Pet::class], version = 4, exportSchema = false)
public abstract class PetRoomDatabase : RoomDatabase() {

    abstract fun tokenDao(): TokenDao
    abstract fun petDao(): PetDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: PetRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): PetRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE
                ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        PetRoomDatabase::class.java,
                        "pet_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                    // return instance
                    instance
                }
        }
    }
}

