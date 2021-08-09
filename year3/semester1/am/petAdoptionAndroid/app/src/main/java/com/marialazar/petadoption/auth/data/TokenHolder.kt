package com.marialazar.petadoption.auth.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "token_holder")
data class TokenHolder(
    @PrimaryKey @ColumnInfo(name = "token") val token: String
)
