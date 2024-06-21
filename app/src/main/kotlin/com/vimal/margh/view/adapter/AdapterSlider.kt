package com.vimal.margh.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.vimal.margh.R
import com.vimal.margh.databinding.ItemViewpagerBinding
import com.vimal.margh.model.ModelMeal
import com.vimal.margh.util.Utils

class AdapterSlider(private val context: Context) :
    RecyclerView.Adapter<AdapterSlider.ViewHolder>() {

    private var list = listOf<ModelMeal>()

    private var onItemClick: ((ModelMeal) -> Unit)? = null
    fun setOnItemClickListener(listener: (ModelMeal) -> Unit) {
        onItemClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemViewpagerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modelList = list[position]

        holder.binding.bannerTitle.text = modelList.strMeal

        holder.binding.bannerMore.setCardBackgroundColor(
            Utils.getMultipleColor(
                position,
                holder.itemView.context
            )
        )

        Glide.with(context)
            .load(modelList.strMealThumb)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.binding.bannerImg)

        holder.binding.banner.setOnClickListener {
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


    class ViewHolder(val binding: ItemViewpagerBinding) : RecyclerView.ViewHolder(binding.root)
}
