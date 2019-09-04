package com.arnold.mvvmcomponent.login.app

import android.app.Application
import android.content.Context
import android.support.v4.app.FragmentManager
import com.arnold.common.architecture.base.delegate.AppLifecycles
import com.arnold.common.architecture.di.module.GlobalConfigModule
import com.arnold.common.architecture.integration.ConfigModule
import com.arnold.common.architecture.utils.LogUtil

/**
 *  * 组件的全局配置信息在此配置, 需要将此实现类声明到 AndroidManifest 中
 * CommonSDK 中已有 [com.arnold.common.sdk.core.GlobalConfiguration] 配置有所有组件都可公用的配置信息
 * 这里用来配置一些组件自身私有的配置信息
 */
class GlobalConfiguration : ConfigModule {

    override var mLoadWeight: Int = 1

    override fun applyOptions(context: Context, builder: GlobalConfigModule.Builder) {
        LogUtil.i("GlobalConfiguration")
    }

    override fun injectAppLifecycle(context: Context, lifecycles: MutableList<AppLifecycles>) {

    }

    override fun injectActivityLifecycle(
        context: Context,
        lifecycles: MutableList<Application.ActivityLifecycleCallbacks>
    ) {

    }

    override fun injectFragmentLifecycle(
        context: Context,
        lifecycles: MutableList<FragmentManager.FragmentLifecycleCallbacks>
    ) {

    }
}