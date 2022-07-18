package com.flourenco.movieschallenge.ui.show

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flourenco.movieschallenge.R
import com.flourenco.movieschallenge.databinding.ItemShowPageBinding
import com.flourenco.movieschallenge.model.Show
import com.flourenco.movieschallenge.utils.setOnClickListenerWithHaptic

class ShowPageAdapter(
    private val listener: Listener
    ): ListAdapter<Show, ShowPageAdapter.ViewHolder>(ShowDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemShowPageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(currentList[position])
    }

    inner class ViewHolder(
        private val binding: ItemShowPageBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bindView(show: Show) {
            Glide
                .with(binding.root)
                .load(show.imagerUrl)
                .centerCrop()
                .into(binding.pageShowImage)
            binding.pageShowTitleText.text = show.name
            binding.pageShowRatingText.text = if (show.rating.isNullOrEmpty()) {
                itemView.context.getString(R.string.show_page_no_rating_text)
            } else {
                show.rating
            }
            binding.pageShowDurationText.text = if (show.duration.isNullOrEmpty()) {
                itemView.context.getString(R.string.show_page_no_duration_text)
            } else {
                show.duration
            }
            binding.pageShowGenreText.text = if (show.genres.isNullOrEmpty()) {
                itemView.context.getString(R.string.show_page_no_genres_text)
            } else {
                show.genres
            }
            binding.pageShowStarsText.text = if (show.stars.isNullOrEmpty()) {
                itemView.context.getString(R.string.show_page_no_stars_text)
            } else {
                show.stars
            }
            binding.pageShowDescriptionText.text = if (show.description.isNullOrEmpty()) {
                itemView.context.getString(R.string.show_page_no_description_text)
            } else {
                show.description
            }
            binding.root.setOnClickListenerWithHaptic {
                listener.onShowPageClicked(show)
            }
        }
    }

    interface Listener {
        fun onShowPageClicked(show: Show)
    }
}