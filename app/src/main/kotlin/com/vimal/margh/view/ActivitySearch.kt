package com.vimal.margh.view


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.vimal.margh.view.adapter.AdapterSearch
import com.vimal.margh.databinding.ActivitySearchBinding
import com.vimal.margh.viewmodel.MainViewModel

class ActivitySearch : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val viewModel: MainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }

    private lateinit var adapter: AdapterSearch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()

        adapter = AdapterSearch(this, 1)
        binding.rvRecycler.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.rvRecycler.adapter = adapter


        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {
                c.let {
                    viewModel.fetchSearch(c.toString())
                }
            }

            override fun beforeTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {
                c.let {
                    viewModel.fetchSearch(c.toString())
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        viewModel.fetchSearch(binding.etSearch.text.toString())


        viewModel.isLoading.observe(this) { isLoading ->
            binding.ivInclude.pvProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.isNoNetwork.observe(this) { isNoNetwork ->
            binding.ivInclude.llErrorNetwork.visibility =
                if (isNoNetwork) View.VISIBLE else View.GONE
        }

        viewModel.isEmpty.observe(this) { isEmpty ->
            binding.ivInclude.llErrorEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
            binding.rvRecycler.visibility = if (isEmpty) View.GONE else View.VISIBLE
        }

        binding.ivInclude.btErrorNetwork.setOnClickListener {
            viewModel.fetchSearch(binding.etSearch.text.toString())
        }

        viewModel.search.observe(this) { list ->
            list.let {
                adapter.setData(list)
            }
        }
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
