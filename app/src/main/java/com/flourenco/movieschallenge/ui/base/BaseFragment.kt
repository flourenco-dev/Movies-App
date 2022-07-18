package com.flourenco.movieschallenge.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    private var navigation: BaseNavigation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigation = activity as? BaseNavigation
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupListeners()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        releaseViewBinding()
    }

    protected fun navigateBack() {
        navigation?.goBack()
    }

    abstract fun setupUi()
    abstract fun setupListeners()
    abstract fun setupObservers()
    abstract fun releaseViewBinding()
}