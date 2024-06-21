package com.vimal.margh.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.vimal.margh.R
import com.vimal.margh.databinding.FragmentGuideTwoBinding

class FragmentGuideTwo : Fragment() {
    private var binding: FragmentGuideTwoBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGuideTwoBinding.inflate(inflater, container, false)

        binding!!.welcomeStart.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_ft_guide_two_to_ft_guide_three)
        }
        return binding!!.getRoot()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
