package com.vimal.margh.view.fragment

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.vimal.margh.databinding.FragmentRecyclerBinding
import com.vimal.margh.model.ModelCategory
import com.vimal.margh.util.Constant
import com.vimal.margh.view.ActivityMore
import com.vimal.margh.view.adapter.AdapterCategory
import com.vimal.margh.viewmodel.RoomViewModel

class FragmentCategory : Fragment() {

    private lateinit var adapter: AdapterCategory
    private val viewModelRoom: RoomViewModel by lazy { ViewModelProvider(this)[RoomViewModel::class.java] }

    private var _binding: FragmentRecyclerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecyclerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AdapterCategory(requireActivity())
        binding.rvRecycler.setLayoutManager(
            StaggeredGridLayoutManager(
                Constant.CATEGORY_COLUMN_COUNT,
                StaggeredGridLayoutManager.VERTICAL
            )
        )
        binding.rvRecycler.adapter = adapter

        adapter.setOnItemClickListener(object : AdapterCategory.OnItemClickListener {
            override fun onItemClick(modelCategory: ModelCategory) {
                val intent = Intent(requireActivity(), ActivityMore::class.java)
                intent.putExtra(Constant.EXTRA_KEY_NAME, modelCategory.strCategory)

                startActivity(
                    intent,
                    ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle()
                )
            }
        })

        viewModelRoom.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.ivInclude.pvProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModelRoom.isNoNetwork.observe(viewLifecycleOwner) { isNoNetwork ->
            binding.ivInclude.llErrorNetwork.visibility =
                if (isNoNetwork) View.VISIBLE else View.GONE
        }

        viewModelRoom.isEmpty.observe(viewLifecycleOwner) { isEmpty ->
            binding.ivInclude.llErrorEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
        }

        binding.ivInclude.btErrorNetwork.setOnClickListener {
            viewModelRoom.fetchCategories()
        }

        viewModelRoom.categories.observe(viewLifecycleOwner) { categories ->
            categories.let {
                adapter.setData(categories)
            }
        }
    }
}