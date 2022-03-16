package com.lahsuak.apps.githuboauth.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lahsuak.apps.githuboauth.R
import com.lahsuak.apps.githuboauth.databinding.FragmentRepositoryBinding
import com.lahsuak.apps.githuboauth.ui.adapter.RepositoryAdapter
import com.lahsuak.apps.githuboauth.ui.viewmodel.RepositoryViewModel
import com.lahsuak.apps.githuboauth.utils.Constants.SORT_ORDER
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RepositoryFragment : Fragment(R.layout.fragment_repository) {

    private lateinit var binding: FragmentRepositoryBinding
    private val viewModel by viewModels<RepositoryViewModel>()
    private var language = mutableListOf<String>()
    private var order: Int = 0

    @Inject
    lateinit var sharePref: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRepositoryBinding.bind(view)

        val adapter = RepositoryAdapter()
        binding.rvRepository.adapter = adapter

        language.add("c")
        language.add("c++")
        language.add("java")
        language.add("javascript")
        language.add("kotlin")
        language.add("python")
        order = sharePref.getInt(SORT_ORDER, 0)

        sort()

        if (arguments != null) {
            try {
                val accessToken: String = RepositoryFragmentArgs.fromBundle(
                    requireArguments()
                ).accessToken
                accessToken.let {
                    viewModel.getUserData(it)
                    viewModel.getData(language[order])
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        viewModel.repositories.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.rvRepository.isVisible = true
            binding.pbRepo.isGone = true
        }
    }

    private fun sort() {
        val sortAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.sort_array)
        )
        sortAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        binding.spinnerSort.adapter = sortAdapter

        //we need by default selected sort when app start
        binding.spinnerSort.setSelection(order)

        binding.spinnerSort.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>,
                    view: View?, item: Int, l: Long
                ) {
                    if (order != item) {
                        order = item
                        viewModel.getData(language[order])
                        binding.rvRepository.isGone = true
                        binding.pbRepo.isVisible = true
                    }
                    sharePref.edit().putInt(SORT_ORDER, order).apply()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
    }
}