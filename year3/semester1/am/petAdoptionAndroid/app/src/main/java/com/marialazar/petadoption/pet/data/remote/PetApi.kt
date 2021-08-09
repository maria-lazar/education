package com.marialazar.petadoption.pet.data.remote

import com.marialazar.petadoption.core.Api
import com.marialazar.petadoption.pet.data.Pet
import retrofit2.http.*

object PetApi {

    interface Service {
        @GET("/api/pet")
        suspend fun find(): List<Pet>

        @GET("/api/pet/{id}")
        suspend fun read(@Path("id") petId: String): Pet;


        @Headers("Content-Type: application/json")
        @POST("/api/pet")
        suspend fun create(@Body pet: Pet): Pet

        @Headers("Content-Type: application/json")
        @PUT("/api/pet/{id}")
        suspend fun update(@Path("id") petId: String, @Body pet: Pet): Pet
    }

    val service: Service = Api.retrofit.create(Service::class.java)
}