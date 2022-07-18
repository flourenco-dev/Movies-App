package com.flourenco.movieschallenge.ui.base

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import com.flourenco.movieschallenge.databinding.ViewSearchInputBinding
import com.flourenco.movieschallenge.utils.get
import com.flourenco.movieschallenge.utils.gone
import com.flourenco.movieschallenge.utils.hideKeyboard
import com.flourenco.movieschallenge.utils.invisible
import com.flourenco.movieschallenge.utils.setOnClickListenerWithHaptic
import com.flourenco.movieschallenge.utils.visible

class SearchInputView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    constructor(context: Context) : this(context, null)

    private var binding: ViewSearchInputBinding? = null
    private var listener: Listener? = null
    private val loadingHandler = Handler(Looper.getMainLooper())

    init {
        binding = ViewSearchInputBinding.inflate(LayoutInflater.from(context), this)
        setupListeners()
    }

    private fun setupListeners() {
        binding?.searchInputCancelButton?.setOnClickListenerWithHaptic {
            binding?.searchInputEditText?.setText("")
        }
        binding?.searchInputEditText?.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                binding?.searchInputCancelButton?.invisible()
            } else {
                binding?.searchInputCancelButton?.visible()
            }
        }
        binding?.searchInputEditText?.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                listener?.onSearchButtonPressed(v.text?.toString().get())
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun setHintText(@StringRes hintResId: Int) {
        binding?.searchInputEditText?.setHint(hintResId)
    }

    fun clearSearchInputFocus() {
        binding?.searchInputEditText?.clearFocus()
        binding?.searchInputEditText?.hideKeyboard()
    }

    fun showSearchLoading() {
        val loadingDelayMillis = 500L
        loadingHandler.postDelayed(
            {
                binding?.searchInputProgressBar?.visible()
                binding?.searchInputIconImage?.gone()
            },
            loadingDelayMillis
        )
    }

    fun hideSearchLoading() {
        loadingHandler.removeCallbacksAndMessages(null)
        binding?.searchInputProgressBar?.gone()
        binding?.searchInputIconImage?.visible()
    }

    interface Listener {
        fun onSearchButtonPressed(text: String)
    }
}