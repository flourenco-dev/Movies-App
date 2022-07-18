package com.flourenco.movieschallenge.ui.trending

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.flourenco.movieschallenge.databinding.FragmentTrendingBinding
import com.flourenco.movieschallenge.model.GetTrendingShowsResult
import com.flourenco.movieschallenge.model.Show
import com.flourenco.movieschallenge.ui.MainViewModel
import com.flourenco.movieschallenge.ui.base.BaseFragment
import com.flourenco.movieschallenge.ui.show.ShowPageAdapter
import com.flourenco.movieschallenge.utils.get
import com.flourenco.movieschallenge.utils.gone
import com.flourenco.movieschallenge.utils.visible
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TrendingFragment: BaseFragment(), ShowPageAdapter.Listener {

    private var binding: FragmentTrendingBinding? = null
    private var navigation: TrendingNavigation? = null
    private val viewModel: MainViewModel by sharedViewModel()
    private lateinit var adapter: ShowPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigation = activity as? TrendingNavigation
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrendingBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun setupUi() {
        adapter = ShowPageAdapter(listener = this)
        binding?.trendingRecycler?.adapter = adapter
        binding?.trendingRecycler?.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        PagerSnapHelper().attachToRecyclerView(binding?.trendingRecycler)
    }

    override fun setupListeners() {
        binding?.trendingSwipeRefreshLayout?.setOnRefreshListener {
            viewModel.triggerGetTrendingShows()
        }
    }

    override fun setupObservers() {
        viewModel.trendingShowsResultObservable.observe(viewLifecycleOwner) {
            if (binding?.trendingSwipeRefreshLayout?.isRefreshing.get()) {
                binding?.trendingSwipeRefreshLayout?.isRefreshing = false
            }
            setupShowsUi(it)
        }
        viewModel.trendingShowsResultObservable.value.let {
            if (it == null ||
                it is GetTrendingShowsResult.Failed ||
                (it is GetTrendingShowsResult.Success && it.shows.isEmpty())
            ) {
                viewModel.triggerGetTrendingShows()
            }
        }
    }

    private fun setupShowsUi(showsResult: GetTrendingShowsResult) {
        if (showsResult is GetTrendingShowsResult.Success) {
            adapter.submitList(showsResult.shows)
            binding?.trendingRecycler?.visible()
            binding?.trendingEmptyView?.gone()
            binding?.trendingRecycler?.smoothScrollToPosition(0)
        } else {
            adapter.submitList(emptyList())
            binding?.trendingRecycler?.gone()
            binding?.trendingEmptyView?.visible()
        }
    }

    override fun releaseViewBinding() {
        binding = null
    }

    override fun onShowPageClicked(show: Show) {
        navigation?.goToDetailFromTrending(show.id)
    }
}