package com.vimal.margh.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vimal.margh.databinding.FragmentGuideThreeBinding
import com.vimal.margh.util.SharedPrefer
import com.vimal.margh.view.MainActivity

class FragmentGuideThree : Fragment() {
    private var binding: FragmentGuideThreeBinding? = null

    private val sharedPrefer: SharedPrefer by lazy { SharedPrefer(requireActivity()) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGuideThreeBinding.inflate(inflater, container, false)

        binding!!.welcomeStart.setOnClickListener {
            sharedPrefer.saveOnBoarding(true)
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }

        return binding!!.getRoot()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
