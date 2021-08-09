package com.marialazar.petadoption.pet.pet

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.marialazar.petadoption.PetApplication
import com.marialazar.petadoption.R
import com.marialazar.petadoption.core.TAG
import com.marialazar.petadoption.pet.pets.PetListViewModel
import com.marialazar.petadoption.pet.pets.PetViewModelFactory
import kotlinx.android.synthetic.main.fragment_pet_edit.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class PetEditFragment : Fragment() {
    companion object {
        const val PET_ID = "PET_ID"
    }

    private val viewModel: PetEditViewModel by viewModels {
        PetEditViewModelFactory(
            (activity?.application as PetApplication).petRepository
        )
    }

    private var petId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate")
        arguments?.let {
            if (it.containsKey(PET_ID)) {
                petId = it.getString(PET_ID).toString()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG, "onCreateView")
        val v = inflater.inflate(R.layout.fragment_pet_edit, container, false)
//        setHasOptionsMenu(true);

        val typeSpinner: Spinner? = v.findViewById(R.id.type_spinner)
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.type_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                if (typeSpinner != null) {
                    typeSpinner.adapter = adapter
                }
            }
        }
        val vaccinatedSpinner: Spinner? = v.findViewById(R.id.vaccinated_spinner)
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.vaccinated_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                if (vaccinatedSpinner != null) {
                    vaccinatedSpinner.adapter = adapter
                }
            }
        }
        return v;
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.menu_user, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        setupViewModel()
        fab.setOnClickListener {
            Log.v(TAG, "save pet")
            viewModel.saveOrUpdatePet(
                pet_name.text.toString(),
                pet_description.text.toString(),
                pet_birthDate.text.toString(),
                pet_weight.text.toString(),
                pet_breed.text.toString(),
                pet_owner.text.toString(),
                type_spinner.selectedItem.toString(),
                vaccinated_spinner.selectedItem.toString()
            )
        }
    }

    private fun setupViewModel() {
        viewModel.pet.observe(viewLifecycleOwner) { pet ->
            Log.v(TAG, "update pet")
            pet_name.setText(pet.name)
            pet_description.setText(pet.description)

            val spinner = type_spinner
            var index = 0
            for (i in 0 until spinner.count) {
                if (spinner.getItemAtPosition(i) == pet.type) {
                    index = i
                }
            }
            type_spinner.setSelection(index)
            if (pet.vaccinated) {
                vaccinated_spinner.setSelection(0)
            } else {
                vaccinated_spinner.setSelection(1)
            }
            pet_breed.setText(pet.breed)
            pet_weight.setText(pet.weight.toString())
            pet_birthDate.setText(SimpleDateFormat("MM/dd/yyyy").format(Date(pet.birthDate)))
            pet_owner.setText(pet.ownerName)
        }
        viewModel.fetching.observe(viewLifecycleOwner) { fetching ->
            Log.v(TAG, "update fetching")
            progress.visibility = if (fetching) View.VISIBLE else View.GONE
        }
        viewModel.fetchingError.observe(viewLifecycleOwner) { exception ->
            if (exception != null) {
                Log.v(TAG, "update fetching error")
                val message = "Fetching exception ${exception.message}"
                val parentActivity = activity
                if (parentActivity != null) {
                    Toast.makeText(parentActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.completed.observe(viewLifecycleOwner) { completed ->
            if (completed) {
                Log.v(TAG, "completed, navigate back")
                findNavController().navigateUp()
            }
        }
        val id = petId
        if (id != null) {
            pet_owner.isEnabled = false
            viewModel.loadPet(id)
        } else {
            pet_owner.visibility = View.GONE
            owner_text.visibility = View.GONE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v(TAG, "onViewCreated")
    }
}