package com.vimal.margh.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.tabs.TabLayoutMediator
import com.vimal.margh.databinding.ActivityMoreBinding
import com.vimal.margh.databinding.ItemTabBinding
import com.vimal.margh.model.ModelCategory
import com.vimal.margh.util.Constant
import com.vimal.margh.view.adapter.AdapterMore
import com.vimal.margh.viewmodel.MainViewModel
import com.vimal.margh.viewmodel.RoomViewModel

class ActivityMore : AppCompatActivity() {

    private lateinit var binding: ActivityMoreBinding
    private val viewModelRoom: RoomViewModel by lazy { ViewModelProvider(this)[RoomViewModel::class.java] }
    private val viewModel: MainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()

        val category = intent.getStringExtra(Constant.EXTRA_KEY_NAME)
        binding.tabLayout.visibility = View.INVISIBLE

        viewModelRoom.fetchCategories()

        viewModelRoom.categories.observe(this) { categories ->
            binding.tabLayout.visibility = View.VISIBLE
            val adapter = AdapterMore(this, categories)
            binding.viewPager.adapter = adapter
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                tab.customView = createCustomTabView(categories[position])
            }.attach()

            val initialPosition = categories.indexOfFirst { it.strCategory == category }
            if (initialPosition != -1) {
                binding.viewPager.setCurrentItem(initialPosition, false)
            }
        }

        binding.ivInclude.btErrorNetwork.setOnClickListener {
            category?.let { viewModel.fetchData(it) }
            viewModelRoom.fetchCategories()
        }
    }

    private fun createCustomTabView(category: ModelCategory): View {
        val binding = ItemTabBinding.inflate(LayoutInflater.from(this), null, false)
        Glide.with(this)
            .load(category.strCategoryThumb)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .override(100, 100)
            .into(binding.tabImage)
        binding.tabText.text = category.strCategory
        return binding.root
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            finishAfterTransition()
        }
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAfterTransition()
            }
        })
    }
}
