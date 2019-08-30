package com.arnold.common.architecture.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.arnold.common.architecture.integration.cache.Cache
import com.arnold.common.architecture.integration.cache.CacheType
import com.arnold.common.architecture.integration.lifecycle.ActivityLifecycleable
import com.arnold.common.architecture.utils.ArnoldUtils
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

/**
 * 当需要继承特定的[actiivty]第三方库时，自己自定义一个[activity]，再按照[BaseActivity]的格式复制过去
 */
abstract class BaseActivity : AppCompatActivity(), IActivity, ActivityLifecycleable {

    private val mLifecycleSubject = BehaviorSubject.create<ActivityEvent>()
    private val mCache: Cache<String, Any> by lazy {
        ArnoldUtils.obtainAppComponentFromContext(this).cacheFactory()
            .build(CacheType.ACTIVITY_CACHE)
    }

    override fun provideCache(): Cache<String, Any> {
        return mCache
    }

    override fun provideLifecycleSubject(): Subject<ActivityEvent> {
        return mLifecycleSubject
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (enableARouterInject()) {
            ARouter.getInstance().inject(this)
        }
        val layoutResId = initView(savedInstanceState)
        if (layoutResId != 0) {
            setContentView(layoutResId)
        }
        initData(savedInstanceState)

    }

    override fun onDestroy() {
        super.onDestroy()

    }


}