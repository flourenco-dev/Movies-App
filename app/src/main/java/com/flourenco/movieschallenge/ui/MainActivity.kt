package com.flourenco.movieschallenge.ui

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.flourenco.movieschallenge.R
import com.flourenco.movieschallenge.databinding.ActivityMainBinding
import com.flourenco.movieschallenge.model.Detail
import com.flourenco.movieschallenge.model.GetDetailByShowIdResult
import com.flourenco.movieschallenge.model.toJsonString
import com.flourenco.movieschallenge.ui.base.BaseNavigation
import com.flourenco.movieschallenge.ui.favorites.FavoritesNavigation
import com.flourenco.movieschallenge.ui.search.SearchNavigation
import com.flourenco.movieschallenge.ui.trending.TrendingNavigation
import com.flourenco.movieschallenge.utils.detailExtra
import com.flourenco.movieschallenge.utils.gone
import com.flourenco.movieschallenge.utils.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity
    : AppCompatActivity(),
      BaseNavigation,
      FavoritesNavigation,
      SearchNavigation,
      TrendingNavigation
{

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var navController: NavController
    private var loadingDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUi()
        setupObservers()
    }

    private fun setupUi() {
        val mainNavHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainNavHostFragment) as NavHostFragment
        navController = mainNavHostFragment.navController
        binding.mainNavBar.inflateMenu(R.menu.menu_nav_bar)
        binding.mainNavBar.setupWithNavController(navController)
        val toolBarConfiguration = AppBarConfiguration.Builder(
            R.id.favorites_dest,
            R.id.search_dest,
            R.id.trending_dest
        ).build()
        binding.mainToolbar.setupWithNavController(navController, toolBarConfiguration)
        binding.mainToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.detail_dest) {
                binding.mainNavBar.gone()
                binding.mainToolbar.gone()
            } else {
                binding.mainNavBar.visible()
                binding.mainToolbar.visible()
            }
        }
        binding.mainToolbar.inflateMenu(R.menu.menu_tool_bar)
        binding.mainToolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.clear_hidden_show) {
                mainViewModel.clearHiddenShows()
                true
            } else {
                false
            }
        }
    }

    private fun setupObservers() {
        mainViewModel.networkConnectedStatusObservable.observe(this) {
            binding.mainOfflineText.isVisible = it.not()
        }
        mainViewModel.getNetworkStateObservable().observe(this) {
            mainViewModel.triggerCheckConnection()
        }
        mainViewModel.showLoadingObservable.observe(this) {
            showLoading()
        }
        mainViewModel.hideLoadingObservable.observe(this) {
            hideLoading()
        }
        mainViewModel.detailByShowIdResultObservable.observe(this) {
            if (it is GetDetailByShowIdResult.Success) {
                goToDetail(it.detail)
            } else {
                showFailedToGetDetailDialog()
            }
        }
    }

    private fun showLoading() {
        loadingDialog = Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.dialog_loading)
        }
        loadingDialog?.show()
    }

    private fun hideLoading() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    private fun goToDetail(detail: Detail) {
        val extras = Bundle().also {
            it.putString(detailExtra, detail.toJsonString())
        }
        navController.navigate(R.id.navigate_to_detail, extras)
    }

    private fun showFailedToGetDetailDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.detail_dialog_request_failed_title))
            .setMessage(getString(R.string.detail_dialog_request_failed_message))
            .setPositiveButton(getString(R.string.button_ok_label)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun goBack() {
        navController.navigateUp()
    }

    override fun goToDetailFromFavorites(showId: String) {
        mainViewModel.triggerGetDetailByShowId(showId)
    }

    override fun goToDetailFromSearch(showId: String) {
        mainViewModel.triggerGetDetailByShowId(showId)
    }

    override fun goToDetailFromTrending(showId: String) {
        mainViewModel.triggerGetDetailByShowId(showId)
    }
}