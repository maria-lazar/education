package com.marialazar.petadoption.pet.pets

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.marialazar.petadoption.PetApplication
import com.marialazar.petadoption.R
import com.marialazar.petadoption.core.TAG
import com.marialazar.petadoption.pet.data.PetRepository
import kotlinx.android.synthetic.main.fragment_pet_list.*


class PetListFragment : Fragment() {
    private lateinit var petListAdapter: PetListAdapter;
    private val petsModel: PetListViewModel by viewModels {
        PetViewModelFactory(
            (activity?.application as PetApplication).authRepository,
            (activity?.application as PetApplication).petRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pet_list, container, false)
        setHasOptionsMenu(true);
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_user, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_settings) {
            //TODO
        } else if (id == R.id.action_logout) {
            petsModel.logout()
            findNavController().navigate(R.id.login_fragment)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        setupPetList()
        fab.setOnClickListener {
            Log.v(TAG, "add new pet")
            findNavController().navigate(R.id.pet_edit_fragment)
        }
    }

    private fun setupPetList() {
        petListAdapter = PetListAdapter(this);
        pet_list.adapter = petListAdapter;
        petsModel.pets.observe(viewLifecycleOwner) { pets ->
            Log.v(TAG, "refresh pets")
            petListAdapter.pets = pets
        }
        petsModel.loading.observe(viewLifecycleOwner) { loading ->
            Log.i(TAG, "update loading")
            progress.visibility = if (loading) View.VISIBLE else View.GONE
        }
        petsModel.loadingError.observe(viewLifecycleOwner) { exception ->
            if (exception != null) {
                Log.i(TAG, "update loading error")
//                val message = "Loading exception ${exception.message}"
                val message = "Network error: Unable to load pets"
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        }
        petsModel.loadPets()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "onDestroy")
    }
}