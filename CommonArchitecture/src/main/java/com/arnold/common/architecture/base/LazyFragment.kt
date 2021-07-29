package com.arnold.common.architecture.base

import androidx.fragment.app.Fragment

/**
 * 懒加载
 */
abstract class LazyFragment : Fragment() {
    private var isFirstLoad = true

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            isFirstLoad = false
            lazyLoad()
        }
    }

    abstract fun lazyLoad()
}