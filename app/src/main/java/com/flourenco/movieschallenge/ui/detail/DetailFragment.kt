package com.flourenco.movieschallenge.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.flourenco.movieschallenge.R
import com.flourenco.movieschallenge.databinding.FragmentDetailBinding
import com.flourenco.movieschallenge.model.Detail
import com.flourenco.movieschallenge.model.toDetail
import com.flourenco.movieschallenge.ui.MainViewModel
import com.flourenco.movieschallenge.ui.base.BaseFragment
import com.flourenco.movieschallenge.utils.detailExtra
import com.flourenco.movieschallenge.utils.get
import com.flourenco.movieschallenge.utils.setOnClickListenerWithHaptic
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DetailFragment: BaseFragment() {

    private var binding: FragmentDetailBinding? = null
    private val viewModel: MainViewModel by sharedViewModel()
    private var detail: Detail? = null
    private lateinit var adapter: DetailInfoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detail = arguments?.getString(detailExtra)?.toDetail()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun setupUi() {
        detail?.let {
            adapter = DetailInfoAdapter(detailInfos = it.detailInfos)
            binding?.detailInfoRecycler?.adapter = adapter
            binding?.detailInfoRecycler?.isNestedScrollingEnabled = false
            binding?.detailTitleText?.text = it.name
            binding?.detailImage?.apply {
                Glide
                    .with(requireContext())
                    .load(it.imagerUrl)
                    .centerCrop()
                    .into(this)
            }
            setupFavoriteUi(isFavorite = it.isFavorite)
            setupHiddenUi(isHidden = it.isHidden)
        } ?: navigateBack()
    }

    private fun setupFavoriteUi(isFavorite: Boolean) {
        binding?.detailFavoriteButton?.setImageResource(
            if (isFavorite) {
                R.drawable.ic_favorite_filled
            } else {
                R.drawable.ic_favorite_outlined
            }
        )
    }

    private fun setupHiddenUi(isHidden: Boolean) {
        binding?.detailVisibilityButton?.setImageResource(
            if (isHidden) {
                R.drawable.ic_invisible
            } else {
                R.drawable.ic_visible
            }
        )
    }

    override fun setupListeners() {
        binding?.detailFavoriteButton?.setOnClickListenerWithHaptic {
            detail = detail?.copy(isFavorite = !detail?.isFavorite.get())
            setupFavoriteUi(isFavorite = detail?.isFavorite.get())
            viewModel.changeShowIsFavoriteStatus(detail)
        }
        binding?.detailVisibilityButton?.setOnClickListenerWithHaptic {
            detail = detail?.copy(isHidden = !detail?.isHidden.get())
            setupHiddenUi(isHidden = detail?.isHidden.get())
            viewModel.changeShowIsHiddenStatus(detail)
        }
        binding?.detailCloseButton?.setOnClickListenerWithHaptic {
            navigateBack()
        }
    }

    override fun setupObservers() {}

    override fun releaseViewBinding() {
        binding = null
    }
}