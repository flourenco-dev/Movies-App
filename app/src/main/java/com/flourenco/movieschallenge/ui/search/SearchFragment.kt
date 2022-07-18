package com.flourenco.movieschallenge.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.flourenco.movieschallenge.R
import com.flourenco.movieschallenge.databinding.FragmentSearchBinding
import com.flourenco.movieschallenge.model.SearchShowsResult
import com.flourenco.movieschallenge.model.Show
import com.flourenco.movieschallenge.ui.MainViewModel
import com.flourenco.movieschallenge.ui.base.BaseFragment
import com.flourenco.movieschallenge.ui.base.SearchInputView
import com.flourenco.movieschallenge.ui.show.ShowAdapter
import com.flourenco.movieschallenge.utils.gone
import com.flourenco.movieschallenge.utils.visible
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SearchFragment: BaseFragment(), ShowAdapter.Listener, SearchInputView.Listener {

    private var binding: FragmentSearchBinding? = null
    private var navigation: SearchNavigation? = null
    private val viewModel: MainViewModel by sharedViewModel()
    private lateinit var adapter: ShowAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigation = activity as? SearchNavigation
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun setupUi() {
        adapter = ShowAdapter(listener = this)
        binding?.searchRecycler?.adapter = adapter
        binding?.searchRecycler?.layoutManager = LinearLayoutManager(requireContext())
        binding?.searchInputView?.setHintText(R.string.search_hint_label)
        setupShowsUi()
    }

    private fun setupShowsUi(
        showsResult: SearchShowsResult = SearchShowsResult.MinCharsNotReached
    ) {
        when (showsResult) {
            is SearchShowsResult.Success -> {
                adapter.submitList(showsResult.shows)
                binding?.searchRecycler?.visible()
                binding?.searchEmptyView?.gone()
            }
            SearchShowsResult.MinCharsNotReached -> {
                adapter.submitList(emptyList())
                binding?.searchRecycler?.gone()
                binding?.searchEmptyView?.text = getString(
                    R.string.search_min_chars_not_reached_label,
                    MainViewModel.minCharsToSearch
                )
                binding?.searchEmptyView?.visible()
            }
            SearchShowsResult.Failed -> {
                adapter.submitList(emptyList())
                binding?.searchRecycler?.gone()
                binding?.searchEmptyView?.text = getString(R.string.search_no_results_label)
                binding?.searchEmptyView?.visible()
            }
        }
    }

    override fun setupListeners() {
        binding?.searchInputView?.setListener(this)
    }

    override fun setupObservers() {
        viewModel.searchShowsResultObservable.observe(viewLifecycleOwner) {
            setupShowsUi(it)
            binding?.searchInputView?.hideSearchLoading()
        }
    }

    override fun releaseViewBinding() {
        binding = null
    }

    override fun onShowClicked(show: Show) {
        navigation?.goToDetailFromSearch(show.id)
    }

    override fun onSearchButtonPressed(text: String) {
        viewModel.triggerSearchShowsByName(text)
        binding?.searchInputView?.showSearchLoading()
        binding?.searchInputView?.clearSearchInputFocus()
    }
}