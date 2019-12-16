package com.arnold.common.architecture.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arnold.common.architecture.integration.cache.Cache
import com.arnold.common.architecture.integration.cache.CacheType
import com.arnold.common.architecture.utils.obtainAppComponentFromContext

abstract class BaseFragment : Fragment(), IFragment {

    private var mCache: Cache<String, Any>? = null
    private var contentView: View? = null
    private var isPageVisible: Boolean = false
    private var isFirst = true
    private var isPrepared: Boolean = false


    @Synchronized
    override fun provideCache(): Cache<String, Any> {
        if (mCache == null) {
            mCache = activity!!.obtainAppComponentFromContext().cacheFactory()
                    .build(CacheType.FRAGMENT_CACHE)
        }
        return mCache!!
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isPrepared = true
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        if (contentView == null) {
            val layout = layout()
            return if (layout is View) {
                contentView = layout
                layout
            } else if (layout is Int && layout != 0) {
                val inflaterView = inflater.inflate(layout, container, false)
                contentView = inflaterView
                inflaterView
            } else {
                throw IllegalArgumentException("layout() return type no support, layout = $layout")
            }
        }

        if (contentView?.parent != null) {
            var parent = contentView?.parent
            if (parent is ViewGroup) {
                parent.removeView(contentView)
            }
        }
        return contentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
    }


    override fun onResume() {
        super.onResume()
        if (userVisibleHint) {
            userVisibleHint = true
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            isPageVisible = true
            lazyLoad()
        } else {
            isPageVisible = false
            onInvisible()
        }
    }

    protected fun onVisible(isVisible: Boolean) {
        lazyLoad()
    }

    /**
     * 懒加载
     */
    private fun lazyLoad() {
        if (!isPrepared || !isPageVisible || !isFirst) {
            return
        }
        updateView()
        isFirst = false
    }

    protected open fun updateView() {

    }

    /**
     * fragment被设置为不可见时调用
     */
    protected fun onInvisible() {}

}