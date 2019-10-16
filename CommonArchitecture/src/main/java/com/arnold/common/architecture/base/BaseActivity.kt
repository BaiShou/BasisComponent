package com.arnold.common.architecture.base

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.arnold.common.architecture.integration.cache.Cache
import com.arnold.common.architecture.integration.cache.CacheType
import com.arnold.common.architecture.utils.obtainAppComponentFromContext


/**
 * 当需要继承特定的[actiivty]第三方库时，自己自定义一个[activity]，再按照[BaseActivity]的格式复制过去
 */
abstract class BaseActivity : AppCompatActivity(), IActivity{

    lateinit var context: Activity

    private val mCache: Cache<String, Any> by lazy {
        this.obtainAppComponentFromContext().cacheFactory()
            .build(CacheType.ACTIVITY_CACHE)
    }

    override fun provideCache(): Cache<String, Any> {
        return mCache
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context = this
        val layout = layout()

        if (layout is View) {
            setContentView(layout)
        } else if (layout is Int && layout != 0) {
            setContentView(layout)
        } else {
            throw IllegalArgumentException("layout() return type no support, layout = $layout")
        }

        initView(savedInstanceState)

//        initData(savedInstanceState)
    }

}