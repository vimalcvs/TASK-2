package com.vimal.margh.view


import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.vimal.margh.R
import com.vimal.margh.database.Repository
import com.vimal.margh.databinding.ActivityDetailBinding
import com.vimal.margh.model.ModelDetail
import com.vimal.margh.model.ModelMeal
import com.vimal.margh.util.Constant
import com.vimal.margh.util.Utils
import com.vimal.margh.util.Utils.getParcelableExtraCompat
import com.vimal.margh.view.adapter.AdapterMeal
import com.vimal.margh.viewmodel.MainViewModel
import java.util.Random

class ActivityDetail : AppCompatActivity() {

    lateinit var binding: ActivityDetailBinding
    private val viewModel: MainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }

    private val repository: Repository by lazy { Repository.getInstance(this)!! }

    private var category: String? = null

    private var model: ModelMeal? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()

        model = intent.getParcelableExtraCompat(Constant.EXTRA_MODEL)
        showFavorite(model!!)

        Log.d("TAG", "onCreate: ${model!!.idMeal}")

        val adapter = AdapterMeal(this)
        binding.rvRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvRecycler.adapter = adapter

        binding.clContainer.visibility = View.GONE

        viewModel.detail.observe(this) { data ->
            if (data.isNotEmpty()) {
                binding.clContainer.visibility = View.VISIBLE

                showDetail(data[0])
                category = data[0].strCategory
                viewModel.fetchData(category)
            }
        }

        viewModel.data.observe(this) { data ->
            data[category]?.let { list ->
                adapter.setData(list)
            }
        }

        binding.rvRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvRecycler.adapter = adapter

        adapter.setOnItemClickListener { model ->
            val intent = Intent(this, ActivityDetail::class.java)
            intent.putExtra(Constant.EXTRA_MODEL, model)
            startActivity(
                intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
            )
            Handler(Looper.getMainLooper()).postDelayed({ finish() }, 500)
        }

        binding.ivInclude.btErrorNetwork.setOnClickListener {
            viewModel.fetchDetail(model!!.idMeal.toString())
        }

        viewModel.isLoading.observe(this) { isLoading ->
            Handler(Looper.getMainLooper()).postDelayed({
                binding.ivInclude.pvProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
            }, 200)
        }

        viewModel.isNoNetwork.observe(this) { isNoNetwork ->
            binding.ivInclude.llErrorNetwork.visibility =
                if (isNoNetwork) View.VISIBLE else View.GONE
        }

        viewModel.isEmpty.observe(this) { isEmpty ->
            binding.ivInclude.llErrorEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
        }

        viewModel.fetchDetail(model!!.idMeal.toString())
    }


    private fun showFavorite(model: ModelMeal) {
        if (repository.isFavorite(model.idMeal)) {
            binding.fab.setImageResource(R.drawable.ic_detail_fav_select)
        } else {
            binding.fab.setImageResource(R.drawable.ic_detail_fav_unselect)
        }
        binding.fab.setOnClickListener {
            if (repository.isFavorite(model.idMeal)) {
                repository.deleteFavorite(model)
                binding.fab.setImageResource(R.drawable.ic_detail_fav_unselect)

                Utils.customSnackBarWithAction(
                    this,
                    binding.root,
                    getString(R.string.list_remove_favorites),
                    R.color.color_red
                )

            } else {
                repository.insertFavorite(model)
                binding.fab.setImageResource(R.drawable.ic_detail_fav_select)

                Utils.customSnackBarWithAction(
                    this,
                    binding.root,
                    getString(R.string.list_add_favorites),
                    R.color.color_green
                )

            }
        }


        val tamp = listOf(
            "212°F",
            "190°F",
            "250°F",
            "242°F",
            "152°F"
        )

        val time = listOf(
            "25-min",
            "15-min",
            "45-min",
            "20-min",
            "30-min"
        )

        val rating = 3 + Random().nextFloat() * (5 - 3)
        binding.rbRating.rating = rating
        binding.tvRating.text = rating.toString()

        binding.tvTime.text = time[Random().nextInt(time.size)]
        binding.tvTemp.text = tamp[Random().nextInt(time.size)]

        val random = Random()
        val randomNumber1 = random.nextInt(61) + 10
        val randomNumber2 = random.nextInt(61) + 10
        val randomNumber3 = random.nextInt(61) + 10
        val randomNumber4 = random.nextInt(61) + 10

        val string1 = "Fats: $randomNumber1%"
        val string2 = "Carbohydrates: $randomNumber2%"
        val string3 = "Proteins: $randomNumber3%"
        val string4 = "Calories: $randomNumber4%"

        binding.tvProgress1.text = string1
        binding.tvProgress2.text = string2
        binding.tvProgress3.text = string3
        binding.tvProgress4.text = string4

        binding.lpProgress1.progress = randomNumber1
        binding.lpProgress2.progress = randomNumber2
        binding.lpProgress3.progress = randomNumber3
        binding.lpProgress4.progress = randomNumber4
    }

    private fun showDetail(model: ModelDetail) {
        Glide.with(this).load(model.strMealThumb).diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_placeholder).into(binding.ivImage)

        binding.tvMeal.text = model.strMeal
        binding.tvInstructions.text = model.strInstructions

        binding.cvYoutube.setOnClickListener {
            Utils.openYoutube(
                this,
                model.strYoutube
            )
        }
        binding.cvShare.setOnClickListener { Utils.shareApp(this) }
        binding.cvSource.setOnClickListener { Utils.getShareUrl(this, model.strSource) }

        val ingredients = listOfNotNull(
            model.strIngredient1,
            model.strIngredient2,
            model.strIngredient3,
            model.strIngredient4,
            model.strIngredient5,
            model.strIngredient6,
            model.strIngredient7,
            model.strIngredient8,
            model.strIngredient9,
            model.strIngredient10,
            model.strIngredient11,
            model.strIngredient12,
            model.strIngredient13,
            model.strIngredient14,
            model.strIngredient15,
            model.strIngredient16,
            model.strIngredient17,
            model.strIngredient18,
            model.strIngredient19,
            model.strIngredient20
        ).filter { it.isNotBlank() }

        val ingredient = ingredients.joinToString(":\n")
        if (ingredient.isNotEmpty()) {
            binding.tvIngredients.text = ingredient
        } else {
            binding.tvIngredients.visibility = View.GONE
        }

        val measures = listOfNotNull(
            model.strMeasure1,
            model.strMeasure2,
            model.strMeasure3,
            model.strMeasure4,
            model.strMeasure5,
            model.strMeasure6,
            model.strMeasure7,
            model.strMeasure8,
            model.strMeasure9,
            model.strMeasure10,
            model.strMeasure11,
            model.strMeasure12,
            model.strMeasure13,
            model.strMeasure14,
            model.strMeasure15,
            model.strMeasure16,
            model.strMeasure17,
            model.strMeasure18,
            model.strMeasure19,
            model.strMeasure20
        ).filter { it.isNotBlank() }

        val measure = measures.joinToString("\n")
        if (measure.isNotEmpty()) {
            binding.tvMeasure.text = measure
        } else {
            binding.tvMeasure.visibility = View.GONE
        }

    }


    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finishAfterTransition() }
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAfterTransition()
            }
        })
    }
}
