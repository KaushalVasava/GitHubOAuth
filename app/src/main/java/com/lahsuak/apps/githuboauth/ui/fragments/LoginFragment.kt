package com.lahsuak.apps.githuboauth.ui.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lahsuak.apps.githuboauth.R
import com.lahsuak.apps.githuboauth.databinding.FragmentLoginBinding
import com.lahsuak.apps.githuboauth.ui.viewmodel.LoginViewModel
import com.lahsuak.apps.githuboauth.utils.Constants
import com.lahsuak.apps.githuboauth.utils.Constants.ACCESS_TOKEN
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding

    @Inject
    lateinit var sharedPref: SharedPreferences
    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        binding.btnLogin.setOnClickListener { processLogin() }

        viewModel.accessToken.observe(viewLifecycleOwner) { accessToken ->
            val action =
                LoginFragmentDirections.actionLoginFragmentToRepositoryFragment(
                    accessToken.accessToken
                )
            findNavController().navigate(action)
            sharedPref.edit().putString(ACCESS_TOKEN, accessToken.accessToken).apply()
        }
    }

    override fun onResume() {
        super.onResume()
        val uri: Uri? = requireActivity().intent?.data
        if (uri != null) {
            val code = uri.getQueryParameter("code")
            if (code != null) {
                viewModel.getAccessToken(code)
                Toast.makeText(requireContext(), "Login success!", Toast.LENGTH_SHORT).show()
            } else if ((uri.getQueryParameter("error")) != null) {
                Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun processLogin() {
        val intent = Intent(
            Intent.ACTION_VIEW, Uri.parse(
                "${Constants.oauthLoginURL}?client_id=${Constants.clientID}&scope=repo&redirect_uri=${Constants.redirectUri}"
            )
        )
        startActivity(intent)
    }
}