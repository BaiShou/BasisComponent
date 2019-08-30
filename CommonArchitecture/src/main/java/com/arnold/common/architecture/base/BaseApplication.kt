package com.arnold.common.architecture.base

import android.app.Application
import android.content.Context
import com.arnold.common.architecture.base.delegate.AppDelegate
import com.arnold.common.architecture.base.delegate.AppLifecycles
import com.arnold.common.architecture.di.component.AppComponent
import com.arnold.common.architecture.di.module.GlobalConfigModule
import com.arnold.common.architecture.integration.ConfigModule
import com.arnold.common.architecture.integration.ManifestParser
import com.arnold.common.architecture.utils.Preconditions
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

/**
 * 所有独立的modu都使用此类
 */
class BaseApplication : Application(), App, HasAndroidInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = dispatchingActivityInjector

    private val mModules: MutableList<ConfigModule> by lazy { ManifestParser(this).parse() }

    private val mAppDelegate: AppLifecycles by lazy { AppDelegate(this) }


    /**
     * 这里会在 {@link BaseApplication#onCreate} 之前被调用,可以做一些较早的初始化
     * 常用于 MultiDex 以及插件化框架的初始化
     */
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        mAppDelegate.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        mAppDelegate.onCreate(this)
    }

    /**
     * 在模拟环境中程序终止时会被调用
     */
    override fun onTerminate() {
        super.onTerminate()
        mAppDelegate.onTerminate(this)
    }


    /**
     * 将 {@link AppComponent} 返回出去, 供其它地方使用, {@link AppComponent} 接口中声明的方法所返回的实例, 在 {@link #getAppComponent()} 拿到对象后都可以直接使用
     */
    override fun getAppComponent(): AppComponent {

        Preconditions.checkNotNull(mAppDelegate, "%s cannot be null", AppDelegate::class.java.name)
        Preconditions.checkState(
            mAppDelegate is App,
            "%s must be implements %s",
            mAppDelegate.javaClass.name,
            App::class.java.name
        )

        return (mAppDelegate as App).getAppComponent()
    }

    /**
     * 将app的全局配置信息封装进module(使用Dagger注入到需要配置信息的地方)
     * 需要在AndroidManifest中声明[ConfigModule]的实现类,和Glide的配置方式相似
     *
     * @return GlobalConfigModule
     */
    private fun getGlobalConfigModule(
        context: Context,
        modules: MutableList<ConfigModule>
    ): GlobalConfigModule {
        val builder = GlobalConfigModule.Builder()
        //遍历 ConfigModule 集合, 给全局配置 GlobalConfigModule 添加参数
        for (module in modules) {
            module.applyOptions(context, builder)
        }
        builder.configModules(modules)
        return builder.build()
    }

}