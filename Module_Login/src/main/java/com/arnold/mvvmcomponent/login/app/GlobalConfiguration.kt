package com.arnold.mvvmcomponent.login.app

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.alibaba.android.arouter.launcher.ARouter
import com.arnold.common.architecture.base.delegate.AppLifecycles
import com.arnold.common.architecture.di.module.GlobalConfigModule
import com.arnold.common.architecture.integration.ConfigModule

class GlobalConfiguration : ConfigModule {

    override fun applyOptions(context: Context, builder: GlobalConfigModule.Builder) {

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