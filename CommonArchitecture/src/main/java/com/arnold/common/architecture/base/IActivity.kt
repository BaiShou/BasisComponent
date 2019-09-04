package com.arnold.common.architecture.base

import android.app.Activity
import android.arch.lifecycle.AndroidViewModel
import android.os.Bundle
import com.arnold.common.architecture.di.component.AppComponent
import com.arnold.common.architecture.integration.cache.Cache

interface IActivity {

    /**
     * 提供在 [Activity] 生命周期内的缓存容器, 可向此 [Activity] 存取一些必要的数据
     * 此缓存容器和 [Activity] 的生命周期绑定, 如果 [Activity] 在屏幕旋转或者配置更改的情况下
     * 重新创建, 那此缓存容器中的数据也会被清空, 如果你想避免此种情况请使用 [AndroidViewModel]
     *
     * @return like [LruCache]
     */
    fun provideCache(): Cache<String, Any>

    /**
     * 提供 AppComponent (提供所有的单例对象) 给实现类, 进行 Component 依赖
     *
     * @param appComponent
     */
    fun setupActivityComponent(component: AppComponent){

    }


    fun enableInject(): Boolean = false


    fun enableARouterInject(): Boolean = false


    /**
     * 是否使用 EventBus
     * 确保依赖后, 将此方法返回 true,  会自动检测您依赖的 EventBus, 并自动注册
     *
     * @return 返回 `true`,  会自动注册 EventBus
     */
    fun useEventBus(): Boolean = false


    /**
     * 如果 [layout] 不是Vewi或者(Int && != 0), 框架则不会调用 [Activity.setContentView],throw [IllegalArgumentException]
     */
    fun layout(): Any

    /**
     * 初始化 View,
     *
     * @param savedInstanceState
     * @return
     */
    fun initView(savedInstanceState: Bundle?)

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    fun initData(savedInstanceState: Bundle?)

    /**
     * 这个 Activity 是否会使用 Fragment,框架会根据这个属性判断是否注册 [FragmentManager.FragmentLifecycleCallbacks]
     * 如果返回`false`,那意味着这个 Activity 不需要绑定 Fragment,那你再在这个 Activity 中绑定继承于 [BaseFragment] 的 Fragment 将不起任何作用
     * @see ActivityLifecycle.registerFragmentCallbacks
     * @return
     */
    fun useFragment(): Boolean = true

}