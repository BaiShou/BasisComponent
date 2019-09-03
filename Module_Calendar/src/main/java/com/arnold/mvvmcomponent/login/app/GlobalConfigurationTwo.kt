package com.arnold.mvvmcomponent.login.app

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.arnold.common.architecture.base.delegate.AppLifecycles
import com.arnold.common.architecture.di.module.AppModule
import com.arnold.common.architecture.di.module.GlobalConfigModule
import com.arnold.common.architecture.integration.ConfigModule
import com.arnold.common.architecture.utils.LogUtil
import com.google.gson.GsonBuilder

class GlobalConfigurationTwo : ConfigModule {

    override var mLoadWeight: Int = 2

    override fun applyOptions(context: Context, builder: GlobalConfigModule.Builder) {
       LogUtil.i("GlobalConfigurationTwo")
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