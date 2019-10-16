package com.arnold.common.architecture.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arnold.common.architecture.integration.cache.Cache
import com.arnold.common.architecture.integration.cache.CacheType
import com.arnold.common.architecture.utils.obtainAppComponentFromContext

abstract class BaseFragment : Fragment(), IFragment {

    private var mCache: Cache<String, Any>? = null
    private var contentView: View? = null



    @Synchronized
    override fun provideCache(): Cache<String, Any> {
        if (mCache == null) {
            mCache = activity!!.obtainAppComponentFromContext().cacheFactory()
                .build(CacheType.FRAGMENT_CACHE)
        }
        return mCache!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (contentView == null) {
            val layout = layout()
            return if (layout is View) {
                initView(layout, savedInstanceState)
                contentView = layout
                layout
            } else if (layout is Int && layout != 0) {
                val inflaterView = inflater.inflate(layout, container, false)
                contentView = inflaterView
                initView(inflaterView, savedInstanceState)
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

}