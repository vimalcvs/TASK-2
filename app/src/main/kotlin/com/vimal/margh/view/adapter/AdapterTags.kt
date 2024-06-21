package com.vimal.margh.view.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vimal.margh.databinding.ItemTagsBinding
import com.vimal.margh.model.ModelCategory
import com.vimal.margh.util.Utils

class AdapterTags : RecyclerView.Adapter<AdapterTags.ViewHolder>() {
    private var list = listOf<ModelCategory>()

    private var onItemClick: ((ModelCategory) -> Unit)? = null
    fun setOnItemClickListener(listener: (ModelCategory) -> Unit) {
        onItemClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTagsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        holder.binding.tvTitle.text = model.strCategory
        holder.binding.flTags.setOnClickListener { onItemClick?.invoke(model) }


        val tintedColor = Utils.getMultipleColor(position, holder.itemView.context)
        holder.binding.tvTitle.backgroundTintList = ColorStateList.valueOf(tintedColor)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(modelPosts: List<ModelCategory>) {
        this.list = modelPosts
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemTagsBinding) : RecyclerView.ViewHolder(binding.root)
}
