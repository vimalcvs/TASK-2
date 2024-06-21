package com.vimal.margh.view.fragment

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.vimal.margh.database.Repository
import com.vimal.margh.databinding.FragmentRecyclerBinding
import com.vimal.margh.util.Constant
import com.vimal.margh.view.ActivityDetail
import com.vimal.margh.view.adapter.AdapterFavorite

class FragmentFavorite : Fragment() {

    private val repository: Repository by lazy { Repository.getInstance(requireActivity())!! }
    private var binding: FragmentRecyclerBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecyclerBinding.inflate(inflater, container, false)
        val root: View = binding!!.root

        initViews()
        return root
    }

    private fun initViews() {
        val adapter = AdapterFavorite(requireActivity())
        repository.allFavorite().observe(viewLifecycleOwner) { products ->
            if (products.isNotEmpty()) {
                adapter.updateData(products)
                binding!!.ivInclude.llEmptyFavorite.visibility = View.GONE
            } else {
                adapter.updateData(ArrayList())
                binding!!.ivInclude.llEmptyFavorite.visibility = View.VISIBLE
            }
        }

        binding!!.rvRecycler.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        binding!!.rvRecycler.adapter = adapter

        adapter.setOnItemClickListener { model ->
            val intent = Intent(requireActivity(), ActivityDetail::class.java)
            intent.putExtra(Constant.EXTRA_MODEL, model)
            startActivity(
                intent,
                ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle()
            )
        }

    }
}