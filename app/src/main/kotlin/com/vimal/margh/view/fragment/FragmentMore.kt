package com.vimal.margh.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.vimal.margh.databinding.FragmentRecyclerBinding
import com.vimal.margh.view.adapter.AdapterMeal
import com.vimal.margh.viewmodel.MainViewModel

class FragmentMore : Fragment() {

    private val viewModel: MainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }

    private lateinit var adapter: AdapterMeal
    private var category: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        category = arguments?.getString("category")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentRecyclerBinding.inflate(inflater, container, false)
        adapter = AdapterMeal(requireContext(), 1)
        binding.rvRecycler.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.rvRecycler.adapter = adapter
        binding.rvRecycler.isNestedScrollingEnabled = false

        viewModel.data.observe(viewLifecycleOwner) { data ->
            data[category]?.let { meals ->
                adapter.setData(meals)
            }
        }

        binding.ivInclude.btErrorNetwork.setOnClickListener {
            category?.let { viewModel.fetchData(it) }
        }

        viewModel.isLoading.observe(requireActivity()) { isLoading ->
            binding.ivInclude.pvProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.isNoNetwork.observe(requireActivity()) { isNoNetwork ->
            binding.ivInclude.llErrorNetwork.visibility =
                if (isNoNetwork) View.VISIBLE else View.GONE
        }

        viewModel.isEmpty.observe(requireActivity()) { isEmpty ->
            binding.ivInclude.llErrorEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
        }


        category?.let { viewModel.fetchData(it) }

        return binding.root
    }

    companion object {
        fun newInstance(category: String): FragmentMore {
            val fragment = FragmentMore()
            val args = Bundle()
            args.putString("category", category)
            fragment.arguments = args
            return fragment
        }
    }
}
