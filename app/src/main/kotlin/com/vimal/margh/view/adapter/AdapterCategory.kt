package com.vimal.margh.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.vimal.margh.R
import com.vimal.margh.databinding.ItemCategoryBinding
import com.vimal.margh.model.ModelCategory


class AdapterCategory(
    private val context: Context
) : RecyclerView.Adapter<AdapterCategory.ViewHolder>() {
    private var list = listOf<ModelCategory>()

    private var mOnItemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(mItemClickListener: OnItemClickListener?) {
        this.mOnItemClickListener = mItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]

        holder.binding.tvTitle.text = model.strCategory

        Glide.with(context)
            .load(model.strCategoryThumb)
            .placeholder(R.drawable.ic_placeholder)
            .override(400, 400)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.ivImage)

        holder.binding.cvCard.setOnClickListener { mOnItemClickListener?.onItemClick(model) }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(modelPosts: List<ModelCategory>) {
        this.list = modelPosts
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(modelCategory: ModelCategory)
    }

    class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)
}
