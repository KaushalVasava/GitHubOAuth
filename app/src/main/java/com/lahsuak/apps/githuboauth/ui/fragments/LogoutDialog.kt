package com.lahsuak.apps.githuboauth.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.lahsuak.apps.githuboauth.utils.Constants.ACCESS_TOKEN
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LogoutDialog : DialogFragment() {

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val accessToken = sharedPref.getString(ACCESS_TOKEN, null)
        return if (accessToken != null) {
            AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure? You want to logout")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Logout") { _, _ ->
                    logout()
                }
                .create()
        } else {
            Toast.makeText(requireContext(), "Login first!!", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            super.onCreateDialog(savedInstanceState)
        }
    }

    private fun logout() {
        sharedPref.edit().putString(ACCESS_TOKEN, null).apply()
        Toast.makeText(requireContext(), "Logout success!", Toast.LENGTH_SHORT).show()
        val action = LogoutDialogDirections.actionLogoutDialogToLoginFragment()
        findNavController().navigate(action)
    }
}