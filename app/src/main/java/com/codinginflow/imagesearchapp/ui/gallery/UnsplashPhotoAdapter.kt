package com.codinginflow.imagesearchapp.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.codinginflow.imagesearchapp.R
import com.codinginflow.imagesearchapp.data.UnsplashPhoto
import com.codinginflow.imagesearchapp.databinding.ItemUnsplashPhotoBinding

class UnsplashPhotoAdapter(private val listener: OnItemClickListener ) :
    PagingDataAdapter<UnsplashPhoto, UnsplashPhotoAdapter.PhotoViewHolder>(PHOTO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        //Inflate the view
        val binding =
            ItemUnsplashPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        //Get the current item
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    //Make the ViewHolder an inner class so that we can be able to access the adapter class property, listener
    inner class PhotoViewHolder(private val binding: ItemUnsplashPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //ClickListener for the photo/ whole view
        init {
            binding.root.setOnClickListener{
                //Get the position of the view
                val position = bindingAdapterPosition
                //Not -1
                if (position != RecyclerView.NO_POSITION){
                    val item = getItem(position)

                    if (item != null){
                        listener.onItemClick(item)
                    }
                }
            }
        }

        //Bind the data to the view
        fun bind(photo: UnsplashPhoto) {
            binding.apply {
                //Load the image with Glide
                Glide.with(itemView)
                    .load(photo.urls.regular)//Regular Photo url
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(imageView)

                textViewUserName.text = photo.user.username
            }
        }
    }

    //Interface for the onClickListener
    interface OnItemClickListener{
        fun onItemClick(photo: UnsplashPhoto)
    }

    //The DiffUtil
    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<UnsplashPhoto>() {
            override fun areItemsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: UnsplashPhoto, newItem: UnsplashPhoto
            ) =
                oldItem == newItem

        }
    }
}