package com.ibrahimethemsen.firebasecallbackflow.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ibrahimethemsen.firebasecallbackflow.common.setVisibility
import com.ibrahimethemsen.firebasecallbackflow.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getQuotes()
        uiState()
    }

    private fun uiState(){
        viewModel.uiStateLiveData.observe(viewLifecycleOwner){ quote ->
            binding.homeProgress.setVisibility(quote.isLoading)
            quote.errorMessage?.let {
                binding.errorMsg.visibility = View.VISIBLE
                binding.errorMsg.text =  quote.errorMessage
            }
            if (quote.data.isNotEmpty()){
                binding.data.text = quote.data[0].name
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}