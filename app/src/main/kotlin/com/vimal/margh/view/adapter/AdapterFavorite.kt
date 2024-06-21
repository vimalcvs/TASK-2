package com.vimal.margh.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.vimal.margh.R
import com.vimal.margh.databinding.ItemMealBigBinding
import com.vimal.margh.model.ModelMeal

class AdapterFavorite(private val context: Context) :
    RecyclerView.Adapter<AdapterFavorite.ViewHolder>() {

    private var list = listOf<ModelMeal>()

    private var onItemClick: ((ModelMeal) -> Unit)? = null
    fun setOnItemClickListener(listener: (ModelMeal) -> Unit) {
        onItemClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMealBigBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modelList = list[position]
        holder.binding.tvTitle.text = modelList.strMeal

        Glide.with(context)
            .load(modelList.strMealThumb)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.binding.ivImage)

        holder.binding.cvCard.setOnClickListener {
            onItemClick?.invoke(modelList)
        }
    }

    fun updateData(productList: List<ModelMeal>) {
        this.list = productList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }


    class ViewHolder(val binding: ItemMealBigBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}