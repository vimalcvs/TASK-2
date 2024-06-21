package com.vimal.margh.view.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vimal.margh.view.fragment.FragmentMore
import com.vimal.margh.model.ModelCategory

class AdapterMore(activity: AppCompatActivity, private val categories: List<ModelCategory>) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = categories.size

    override fun createFragment(position: Int): Fragment {
        return FragmentMore.newInstance(categories[position].strCategory)
    }
}
