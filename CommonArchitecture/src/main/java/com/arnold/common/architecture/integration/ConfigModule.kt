package com.arnold.common.architecture.integration

import android.app.Activity
import android.app.Application
import android.content.Context
import android.support.v4.app.FragmentManager
import com.arnold.common.architecture.base.delegate.AppLifecycles
import com.arnold.common.architecture.di.module.GlobalConfigModule

interface ConfigModule {

    /**
     * 加载权重，控制不同配置文件加载顺序
     */
    var mLoadWeight: Int

    /**
     * 使用 [GlobalConfigModule.Builder] 给框架配置一些配置参数
     *
     * @param context [Context]
     * @param builder [GlobalConfigModule.Builder]
     */
    abstract fun applyOptions(context: Context, builder: GlobalConfigModule.Builder)

    /**
     * 使用 [AppLifecycles] 在 [Application] 的生命周期中注入一些操作
     *
     * @param context    [Context]
     * @param lifecycles [Application] 的生命周期容器, 可向框架中添加多个 [Application] 的生命周期类
     */
    abstract fun injectAppLifecycle(context: Context, lifecycles: MutableList<AppLifecycles>)

    /**
     * 实例化component
     */
//     fun injectComponent(component: IComponent){}

    /**
     * 使用 [Application.ActivityLifecycleCallbacks] 在 [Activity] 的生命周期中注入一些操作
     *
     * @param context    [Context]
     * @param lifecycles [Activity] 的生命周期容器, 可向框架中添加多个 [Activity] 的生命周期类
     */
    abstract fun injectActivityLifecycle(
        context: Context,
        lifecycles: MutableList<Application.ActivityLifecycleCallbacks>
    )

    /**
     * 使用 [FragmentManager.FragmentLifecycleCallbacks] 在 [Fragment] 的生命周期中注入一些操作
     *
     * @param context    [Context]
     * @param lifecycles [Fragment] 的生命周期容器, 可向框架中添加多个 [Fragment] 的生命周期类
     */
    abstract fun injectFragmentLifecycle(
        context: Context,
        lifecycles: MutableList<FragmentManager.FragmentLifecycleCallbacks>
    )
}