package com.entsh118.housespot.ui.auth.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entsh118.housespot.databinding.ItemSelectedImageBinding

class SelectedImagesAdapter(
    private val imageUris: MutableList<Uri>,
    private val onRemoveImage: (Int) -> Unit
) : RecyclerView.Adapter<SelectedImagesAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemSelectedImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(uri: Uri, position: Int) {
            binding.ivSelectedImage.setImageURI(uri)
            binding.tvFileName.text = uri.lastPathSegment
            binding.btnRemoveImage.setOnClickListener {
                onRemoveImage(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSelectedImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(imageUris[position], position)
    }

    override fun getItemCount(): Int = imageUris.size
}
