package com.flourenco.movieschallenge.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flourenco.movieschallenge.databinding.FragmentFavoritesBinding
import com.flourenco.movieschallenge.model.Show
import com.flourenco.movieschallenge.ui.MainViewModel
import com.flourenco.movieschallenge.ui.base.BaseFragment
import com.flourenco.movieschallenge.ui.show.ShowAdapter
import com.flourenco.movieschallenge.utils.gone
import com.flourenco.movieschallenge.utils.visible
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FavoritesFragment: BaseFragment(), ShowAdapter.Listener {

    private var binding: FragmentFavoritesBinding? = null
    private var navigation: FavoritesNavigation? = null
    private val viewModel: MainViewModel by sharedViewModel()
    private lateinit var adapter: ShowAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigation = activity as? FavoritesNavigation
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun setupUi() {
        adapter = ShowAdapter(listener = this)
        binding?.favoritesRecycler?.adapter = adapter
        setupFavoritesUi()
    }

    private fun setupFavoritesUi(favorites: List<Show>? = null) {
        if (favorites.isNullOrEmpty()) {
            adapter.submitList(emptyList())
            binding?.favoritesRecycler?.gone()
            binding?.favoritesEmptyView?.visible()
        } else {
            adapter.submitList(favorites)
            binding?.favoritesRecycler?.visible()
            binding?.favoritesEmptyView?.gone()
        }
    }

    override fun setupListeners() {}

    override fun setupObservers() {
        viewModel.getFavoritesObservable().observe(viewLifecycleOwner) {
            setupFavoritesUi(it)
        }
    }

    override fun releaseViewBinding() {
        binding = null
    }

    override fun onShowClicked(show: Show) {
        navigation?.goToDetailFromFavorites(show.id)
    }
}