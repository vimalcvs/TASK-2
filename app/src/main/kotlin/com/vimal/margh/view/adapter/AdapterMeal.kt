package com.vimal.margh.view.adapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.vimal.margh.R
import com.vimal.margh.databinding.ItemMealBigBinding
import com.vimal.margh.databinding.ItemMealSmallBinding
import com.vimal.margh.model.ModelMeal
import com.vimal.margh.util.Constant
import com.vimal.margh.view.ActivityDetail
import java.util.Random


class AdapterMeal(
    private val context: Context, private val type: Int = 0
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var list = listOf<ModelMeal>()

    private var onItemClick: ((ModelMeal) -> Unit)? = null
    fun setOnItemClickListener(listener: (ModelMeal) -> Unit) {
        onItemClick = listener
    }

    override fun getItemViewType(position: Int): Int {
        return type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> ViewHolderList(
                ItemMealBigBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> ViewHolderGrid(
                ItemMealSmallBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val modelList = list[position]

        when (holder) {
            is ViewHolderGrid -> {
                holder.binding.exploreItemText.text = modelList.strMeal

                Glide.with(context)
                    .load(modelList.strMealThumb)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(holder.binding.exploreItemImg)



                holder.binding.exploreItem.setOnClickListener {
                    onItemClick?.invoke(modelList)
                }
            }

            is ViewHolderList -> {
                holder.binding.tvTitle.text = modelList.strMeal

                val rating = 3 + Random().nextFloat() * (5 - 3)
                holder.binding.rbRating.rating = rating

                val random = Random()
                val randomIndex = random.nextInt(chefNames.size)
                holder.binding.tvCategory.text = chefNames[randomIndex]

                Glide.with(context)
                    .load(modelList.strMealThumb)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(holder.binding.ivImage)

                holder.binding.cvCard.setOnClickListener {
                    val intent = Intent(context, ActivityDetail::class.java)
                    intent.putExtra(Constant.EXTRA_MODEL, modelList)
                    context.startActivity(
                        intent,
                        ActivityOptions.makeSceneTransitionAnimation(context as Activity?)
                            .toBundle()
                    )
                }
            }
        }
    }

    private val chefNames = listOf(
        "Sanjeev Kapoor",
        "Vikas Khanna",
        "Ranveer Brar",
        "Saransh Goila",
        "Kunal Kapur"
    )

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(radioList: List<ModelMeal>) {
        this.list = radioList
        notifyDataSetChanged()
    }

    class ViewHolderList(val binding: ItemMealBigBinding) : RecyclerView.ViewHolder(binding.root)

    class ViewHolderGrid(val binding: ItemMealSmallBinding) : RecyclerView.ViewHolder(binding.root)
}

