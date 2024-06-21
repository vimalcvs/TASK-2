package com.vimal.margh.view.fragment

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.vimal.margh.R
import com.vimal.margh.databinding.FragmentMealBinding
import com.vimal.margh.util.Constant
import com.vimal.margh.view.ActivityDetail
import com.vimal.margh.view.ActivityMore
import com.vimal.margh.view.adapter.AdapterMeal
import com.vimal.margh.view.adapter.AdapterSlider
import com.vimal.margh.view.adapter.AdapterTags
import com.vimal.margh.viewmodel.MainViewModel
import com.vimal.margh.viewmodel.RoomViewModel

class FragmentMeal : Fragment() {

    private val viewModel: MainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }
    private val viewModelRoom: RoomViewModel by lazy { ViewModelProvider(this)[RoomViewModel::class.java] }

    private val sliderHandler = Handler(Looper.getMainLooper())
    private lateinit var adapterSlider: AdapterSlider
    private lateinit var adapterBreakfast: AdapterMeal
    private lateinit var adapterPasta: AdapterMeal
    private lateinit var adapterLamb: AdapterMeal
    private lateinit var adapterPork: AdapterMeal
    private lateinit var adapterSide: AdapterMeal
    private lateinit var adapterVegetarian: AdapterMeal
    private lateinit var adapterCategory: AdapterTags

    private var _binding: FragmentMealBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapters()
        setupObservers()
        fetchInitialData()

        showMoreList()
        updateAdapter()
        adapterIntent()
        showSlider()

        binding.swipeRefresh.setOnRefreshListener {
            fetchInitialData()
            binding.swipeRefresh.isRefreshing = false
        }
        binding.swipeRefresh.setColorSchemeResources(
            R.color.color_red,
            R.color.color_green,
            R.color.color_blue,
            R.color.color_orange
        )
    }

    private fun setupAdapters() {
        adapterBreakfast = AdapterMeal(requireActivity())
        adapterPasta = AdapterMeal(requireActivity())
        adapterLamb = AdapterMeal(requireActivity())
        adapterPork = AdapterMeal(requireActivity())
        adapterSide = AdapterMeal(requireActivity())
        adapterVegetarian = AdapterMeal(requireActivity())
        adapterCategory = AdapterTags()
        adapterSlider = AdapterSlider(requireActivity())
    }

    private fun setupObservers() {
        viewModelRoom.categories.observe(viewLifecycleOwner) { categories ->
            adapterCategory.setData(categories)
        }

        viewModelRoom.slider.observe(viewLifecycleOwner) { slider ->
            adapterSlider.updateData(slider)
            setupIndicator(slider.size)
        }


        viewModel.breakfast.observe(requireActivity()) { meals ->
            meals?.let {
                adapterBreakfast.setData(it)
            }
        }
        viewModel.pasta.observe(requireActivity()) { meals ->
            meals?.let {
                adapterPasta.setData(it)
            }
        }
        viewModel.lamb.observe(requireActivity()) { meals ->
            meals?.let {
                adapterLamb.setData(it)
            }
        }

        viewModel.pork.observe(requireActivity()) { meals ->
            meals?.let {
                adapterPork.setData(it)
            }
        }

        viewModel.side.observe(requireActivity()) { meals ->
            meals?.let {
                adapterSide.setData(it)
            }
        }

        viewModel.vegetarian.observe(requireActivity()) { meals ->
            meals?.let {
                adapterVegetarian.setData(it)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.ivInclude.pvProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.isNoNetwork.observe(viewLifecycleOwner) { isNoNetwork ->
            binding.ivInclude.llErrorNetwork.visibility =
                if (isNoNetwork) View.VISIBLE else View.GONE
        }

        viewModel.isEmpty.observe(viewLifecycleOwner) { isEmpty ->
            binding.ivInclude.llErrorEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
        }

        binding.ivInclude.btErrorNetwork.setOnClickListener {
            fetchInitialData()
        }
    }

    private fun fetchInitialData() {
        viewModel.fetchBreakfast()
        viewModel.fetchPasta()
        viewModel.fetchLamb()
        viewModel.fetchPork()
        viewModel.fetchSide()
        viewModel.fetchVegetarian()

        viewModelRoom.fetchCategories()
        viewModelRoom.fetchSlider()
    }

    private fun updateAdapter() {
        binding.rvRecyclerTag.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL)
        binding.rvRecyclerTag.adapter = adapterCategory

        binding.rvRecyclerBreakfast.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
        binding.rvRecyclerBreakfast.adapter = adapterBreakfast

        binding.rvRecyclerPasta.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
        binding.rvRecyclerPasta.adapter = adapterPasta

        binding.rvRecyclerLamb.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
        binding.rvRecyclerLamb.adapter = adapterLamb

        binding.rvRecyclerPork.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
        binding.rvRecyclerPork.adapter = adapterPork

        binding.rvRecyclerSide.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
        binding.rvRecyclerSide.adapter = adapterSide

        binding.rvRecyclerVegetarian.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
        binding.rvRecyclerVegetarian.adapter = adapterVegetarian
    }

    private fun showMoreList() {
        binding.llMoreBreakfast.setOnClickListener {
            val intent = Intent(requireActivity(), ActivityMore::class.java).apply {
                putExtra(Constant.EXTRA_KEY_NAME, "Breakfast")
            }
            startActivity(
                intent,
                ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle()
            )
        }

        binding.llMorePasta.setOnClickListener {
            val intent = Intent(requireActivity(), ActivityMore::class.java).apply {
                putExtra(Constant.EXTRA_KEY_NAME, "Pasta")
            }
            startActivity(
                intent,
                ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle()
            )
        }

        binding.llMoreLamb.setOnClickListener {
            val intent = Intent(requireActivity(), ActivityMore::class.java).apply {
                putExtra(Constant.EXTRA_KEY_NAME, "Lamb")
            }
            startActivity(
                intent,
                ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle()
            )
        }

        binding.llMorePork.setOnClickListener {
            val intent = Intent(requireActivity(), ActivityMore::class.java).apply {
                putExtra(Constant.EXTRA_KEY_NAME, "Pork")
            }
            startActivity(
                intent,
                ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle()
            )
        }

        binding.llMoreSide.setOnClickListener {
            val intent = Intent(requireActivity(), ActivityMore::class.java).apply {
                putExtra(Constant.EXTRA_KEY_NAME, "Side")
            }
            startActivity(
                intent,
                ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle()
            )
        }

        binding.llMoreVegetarian.setOnClickListener {
            val intent = Intent(requireActivity(), ActivityMore::class.java).apply {
                putExtra(Constant.EXTRA_KEY_NAME, "Vegetarian")
            }
            startActivity(
                intent,
                ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle()
            )
        }
    }

    private fun adapterIntent() {
        adapterCategory.setOnItemClickListener { model ->
            val intent = Intent(requireActivity(), ActivityMore::class.java)
            intent.putExtra(Constant.EXTRA_KEY_NAME, model.strCategory)
            startActivity(
                intent,
                ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle()
            )
        }

        adapterBreakfast.setOnItemClickListener { model ->
            val intent = Intent(requireActivity(), ActivityDetail::class.java)
            intent.putExtra(Constant.EXTRA_MODEL, model)
            startActivity(
                intent,
                ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle()
            )
        }

        adapterPasta.setOnItemClickListener { model ->
            val intent = Intent(requireActivity(), ActivityDetail::class.java)
            intent.putExtra(Constant.EXTRA_MODEL, model)
            startActivity(
                intent,
                ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle()
            )
        }

        adapterLamb.setOnItemClickListener { model ->
            val intent = Intent(requireActivity(), ActivityDetail::class.java)
            intent.putExtra(Constant.EXTRA_MODEL, model)
            startActivity(
                intent,
                ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle()
            )
        }

        adapterPork.setOnItemClickListener { model ->
            val intent = Intent(requireActivity(), ActivityDetail::class.java)
            intent.putExtra(Constant.EXTRA_MODEL, model)
            startActivity(
                intent,
                ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle()
            )
        }

        adapterSide.setOnItemClickListener { model ->
            val intent = Intent(requireActivity(), ActivityDetail::class.java)
            intent.putExtra(Constant.EXTRA_MODEL, model)
            startActivity(
                intent,
                ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle()
            )
        }

        adapterVegetarian.setOnItemClickListener { model ->
            val intent = Intent(requireActivity(), ActivityDetail::class.java)
            intent.putExtra(Constant.EXTRA_MODEL, model)
            startActivity(
                intent,
                ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle()
            )
        }

        adapterSlider.setOnItemClickListener { model ->
            val intent = Intent(requireActivity(), ActivityDetail::class.java)
            intent.putExtra(Constant.EXTRA_MODEL, model)
            startActivity(
                intent,
                ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle()
            )
        }
    }

    private fun showSlider() {
        binding.viewPager.apply {
            adapter = adapterSlider
            offscreenPageLimit = 1
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    sliderHandler.removeCallbacks(sliderRunnable)
                    sliderHandler.postDelayed(sliderRunnable, 3000)
                    updateIndicator(position % adapterSlider.itemCount)
                }
            })
        }
        setupIndicator(adapterSlider.itemCount)
    }

    private fun setupIndicator(count: Int) {
        binding.indicatorLayout.removeAllViews()
        val dots = arrayOfNulls<ImageView>(count)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(8, 0, 8, 0)
        }
        for (i in dots.indices) {
            dots[i] = ImageView(requireContext()).apply {
                setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.inactive_dot
                    )
                )
                this.layoutParams = layoutParams
            }
            binding.indicatorLayout.addView(dots[i])
        }
        if (dots.isNotEmpty()) {
            dots[0]?.setImageDrawable(
                ContextCompat.getDrawable(requireContext(), R.drawable.active_dot)
            )
        }
    }

    private fun updateIndicator(index: Int) {
        val childCount = binding.indicatorLayout.childCount
        for (i in 0 until childCount) {
            val imageView = binding.indicatorLayout.getChildAt(i) as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(requireContext(), R.drawable.active_dot)
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(requireContext(), R.drawable.inactive_dot)
                )
            }
        }
    }

    private val sliderRunnable = Runnable {
        binding.viewPager.apply {
            currentItem = (currentItem + 1) % adapterSlider.itemCount
        }
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 3000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sliderHandler.removeCallbacks(sliderRunnable)
        _binding = null
    }
}
