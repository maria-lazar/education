package com.marialazar.petadoption.pet.pets

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.marialazar.petadoption.R
import com.marialazar.petadoption.core.TAG
import com.marialazar.petadoption.pet.data.Pet
import com.marialazar.petadoption.pet.pet.PetEditFragment
import kotlinx.android.synthetic.main.view_pet.view.*
import java.text.SimpleDateFormat
import java.util.*

class PetListAdapter(private val fragment: Fragment) :
    RecyclerView.Adapter<PetListAdapter.ViewHolder>() {

    var pets = emptyList<Pet>()
        set(value) {
            field = value
            notifyDataSetChanged();
        }
    private var onPetClick: View.OnClickListener;

    init {
        onPetClick = View.OnClickListener { view ->
            val pet = view.tag as Pet
            fragment.findNavController().navigate(R.id.pet_edit_fragment, Bundle().apply {
                putString(PetEditFragment.PET_ID, pet._id)
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_pet, parent, false)
        Log.v(TAG, "onCreateViewHolder")
        return ViewHolder(view)
    }

    override fun getItemCount() = pets.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.v(TAG, "onBindViewHolder $position")
        val pet = pets[position]
        holder.itemView.tag = pet
        holder.itemView.setOnClickListener(onPetClick)
        holder.name.text = pet.name
        holder.type.text = pet.type
        holder.breed.text = pet.breed
        holder.dateOfBirth.text =
            "Born on: \n" + SimpleDateFormat("MM/dd/yyyy").format(Date(pet.birthDate))
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.name
        val type: TextView = view.type
        val breed: TextView = view.breed
        val dateOfBirth: TextView = view.dateOfBirth
    }
}