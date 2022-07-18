package com.flourenco.movieschallenge.ui.show

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flourenco.movieschallenge.R
import com.flourenco.movieschallenge.databinding.ItemShowBinding
import com.flourenco.movieschallenge.model.Show
import com.flourenco.movieschallenge.utils.setOnClickListenerWithHaptic

class ShowAdapter(
    private val listener: Listener
    ): ListAdapter<Show, ShowAdapter.ViewHolder>(ShowDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemShowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(currentList[position])
    }

    inner class ViewHolder(
        private val binding: ItemShowBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bindView(show: Show) {
            Glide
                .with(binding.root)
                .load(show.imagerUrl)
                .centerCrop()
                .into(binding.showImage)
            binding.showTitleText.text = show.name
            binding.showRatingText.text = if (show.rating.isNullOrEmpty()) {
                itemView.context.getString(R.string.show_no_rating_text)
            } else {
                show.rating
            }
            binding.showDurationText.text = if (show.duration.isNullOrEmpty()) {
                itemView.context.getString(R.string.show_no_duration_text)
            } else {
                show.duration
            }
            binding.showDescriptionText.text = if (show.description.isNullOrEmpty()) {
                itemView.context.getString(R.string.show_no_description_text)
            } else {
                show.description
            }
            binding.root.setOnClickListenerWithHaptic {
                listener.onShowClicked(show)
            }
        }
    }

    interface Listener {
        fun onShowClicked(show: Show)
    }
}