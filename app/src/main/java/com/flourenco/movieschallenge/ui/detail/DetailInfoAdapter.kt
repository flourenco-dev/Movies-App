package com.flourenco.movieschallenge.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flourenco.movieschallenge.databinding.ItemDetailInfoBinding
import com.flourenco.movieschallenge.model.DetailInfo

class DetailInfoAdapter(
    private val detailInfos: List<DetailInfo>
): RecyclerView.Adapter<DetailInfoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemDetailInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(detailInfos[position])
    }

    override fun getItemCount(): Int = detailInfos.size

    class ViewHolder(
        private val binding: ItemDetailInfoBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bindView(detailInfo: DetailInfo) {
            binding.detailInfoHeader.text = itemView.context.getString(detailInfo.header.headerId)
            binding.detailInfoText.text = detailInfo.content
        }
    }
}