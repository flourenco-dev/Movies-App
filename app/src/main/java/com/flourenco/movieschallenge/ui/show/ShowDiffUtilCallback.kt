package com.flourenco.movieschallenge.ui.show

import androidx.recyclerview.widget.DiffUtil
import com.flourenco.movieschallenge.model.Show

class ShowDiffUtilCallback: DiffUtil.ItemCallback<Show>() {

    override fun areItemsTheSame(oldItem: Show, newItem: Show): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Show, newItem: Show): Boolean = oldItem == newItem
}