package com.arnold.common.architecture.base

import android.app.Activity
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arnold.common.architecture.di.component.AppComponent
import com.arnold.common.architecture.integration.cache.Cache

interface IFragment {
    /**
     * 提供在 [Fragment] 生命周期内的缓存容器, 可向此 [Fragment] 存取一些必要的数据
     * 此缓存容器和 [Fragment] 的生命周期绑定, 如果 [Fragment] 在屏幕旋转或者配置更改的情况下
     * 重新创建, 那此缓存容器中的数据也会被清空, 如果你想避免此种情况请使用 [LifecycleModel](https://github.com/JessYanCoding/LifecycleModel)
     *
     * @return like [LruCache]
     */
    fun provideCache(): Cache<String, Any>


    /**
     * 提供 AppComponent (提供所有的单例对象) 给实现类, 进行 Component 依赖
     *
     * @param appComponent
     */
    fun setupFragmentComponent(appComponent: AppComponent)


    /**
     * 如果 [layout] 不是Vewi或者(Int && != 0), 框架则不会调用 [Activity.setContentView],throw [IllegalArgumentException]
     */
    fun layout(): Any


    /**
     * 初始化 View
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    fun initView(view: View, savedInstanceState: Bundle?)

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    fun initData(savedInstanceState: Bundle?)


    /**
     *
     * [.setData] 框架是不会调用的, 是拿给开发者自己去调用的, 让 [Activity] 或者其他类可以和 [Fragment] 通信,
     * 并且因为 [.setData] 是 [IFragment] 的方法, 所以你可以通过多态, 持有父类,
     * 不持有具体子类的方式就可以和子类 [Fragment] 通信, 这样如果需要替换子类, 就不会影响到其他地方,
     * 并且 [.setData] 可以通过传入 [Message] 作为参数, 使外部统一调用 [.setData],
     * 方法内部再通过 `switch(message.what)` 的方式, 从而在外部调用方式不变的情况下, 却可以扩展更多的方法,
     * 让方法扩展更多的参数, 这样不管 [Fragment] 子类怎么变, 它内部的方法以及方法的参数怎么变, 却不会影响到外部调用的任何一行代码
     *
     * @param data 当不需要参数时 `data` 可以为 `null`
     */
    fun setData(data: Any)


    fun enableInject(): Boolean = false


    fun enableARouterInject(): Boolean = false


    /**
     * 是否使用 EventBus
     * 确保依赖后, 将此方法返回 true,  会自动检测您依赖的 EventBus, 并自动注册
     *
     * @return 返回 `true`,  会自动注册 EventBus
     */
    fun useEventBus(): Boolean = false


}