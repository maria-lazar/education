package com.marialazar.petadoption.pet.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pet")
data class Pet(
    @PrimaryKey @ColumnInfo(name = "id")
    val _id: String,
    var name: String,
    var description: String,
    var type: String,
    var breed: String,
    var vaccinated: Boolean,
    var weight: Float,
    var birthDate: Long,
    var owner: String,
    var ownerName: String,
    var lastModified: Long?,
    var version: Long?
) {
    override fun toString(): String {
        return "Pet(_id='$_id', name='$name', description='$description', type='$type', breed='$breed', vaccinated=$vaccinated, weight=$weight, birthDate=$birthDate, owner='$owner', ownerName='$ownerName')"
    }
}