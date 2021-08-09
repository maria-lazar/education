package com.marialazar.petadoption.auth

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.marialazar.petadoption.PetApplication
import com.marialazar.petadoption.R
import com.marialazar.petadoption.core.Api
import com.marialazar.petadoption.core.Result
import com.marialazar.petadoption.core.TAG
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {
    private val viewModel: LoginViewModel by viewModels {
        AuthViewModelFactory(
            (activity?.application as PetApplication).authRepository,
            (activity?.application as PetApplication).petRepository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.i(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        checkLoggedIn()
    }

    private fun checkLoggedIn() {
        val token = viewModel.token
        if (token != null) {
            loading.visibility = View.VISIBLE
            loginButton.visibility = View.GONE
            username_text.visibility = View.GONE
            password_text.visibility = View.GONE
            token.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    viewModel.startListen()
                    Api.tokenInterceptor.token = it[0].token
                    loading.visibility = View.GONE
                    findNavController().navigate(R.id.action_loginFragment_to_PetListFragment)
                } else {
                    loading.visibility = View.GONE
                    loginButton.visibility = View.VISIBLE
                    username_text.visibility = View.VISIBLE
                    password_text.visibility = View.VISIBLE
                    setupLoginForm()
                }
            }
        } else {
            setupLoginForm()
        }
    }

    private fun setupLoginForm() {
        viewModel.loginResult.observe(viewLifecycleOwner) { loginResult ->
            loading.visibility = View.GONE
            if (loginResult is Result.Success<*>) {
                viewModel.startListen()
                findNavController().navigate(R.id.action_loginFragment_to_PetListFragment)
            } else if (loginResult is Result.Error) {
                //                error_text.text = "Login error ${loginResult.exception.message}"
                error_text.text = "Failed to authenticate"
                error_text.visibility = View.VISIBLE
            }
        }
        loginButton.setOnClickListener {
            loading.visibility = View.VISIBLE
            error_text.visibility = View.GONE
            viewModel.login(username_text.text.toString(), password_text.text.toString())
        }
    }
}