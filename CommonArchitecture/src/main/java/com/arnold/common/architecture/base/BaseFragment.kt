package com.arnold.common.architecture.base

import android.app.PendingIntent.getActivity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arnold.common.architecture.integration.cache.Cache
import com.arnold.common.architecture.integration.cache.CacheType
import com.arnold.common.architecture.integration.lifecycle.FragmentLifecycleable
import com.arnold.common.architecture.utils.obtainAppComponentFromContext
import com.trello.rxlifecycle2.android.FragmentEvent
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

abstract class BaseFragment : Fragment(), IFragment, FragmentLifecycleable {
    private val mLifecycleSubject = BehaviorSubject.create<FragmentEvent>()
    private var mCache: Cache<String, Any>? = null
    protected var mContext: Context? = null

    override fun provideLifecycleSubject(): Subject<FragmentEvent> = mLifecycleSubject

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
        return initView(inflater, container, savedInstanceState)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }
}