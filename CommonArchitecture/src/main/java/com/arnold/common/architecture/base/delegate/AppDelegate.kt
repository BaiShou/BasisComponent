package com.arnold.common.architecture.base.delegate

import android.app.Application
import android.content.Context
import com.arnold.common.architecture.base.App
import com.arnold.common.architecture.di.component.AppComponent
import com.arnold.common.architecture.di.component.DaggerAppComponent
import com.arnold.common.architecture.di.module.GlobalConfigModule
import com.arnold.common.architecture.integration.ConfigModule
import com.arnold.common.architecture.integration.ManifestParser
import com.arnold.common.architecture.integration.cache.IntelligentCache
import com.arnold.common.architecture.utils.Preconditions
import com.arnold.common.architecture.utils.getProcessName
import com.tencent.mmkv.MMKV
import java.util.*
import javax.inject.Inject
import javax.inject.Named


/**
 * AppDelegate 可以代理 Application 的生命周期,在对应的生命周期,执行对应的逻辑
 */
class AppDelegate(context: Context) : AppLifecycles, App {

    private var mApplication: Application? = null
    private var mAppComponent: AppComponent? = null

    private var mModules: MutableList<ConfigModule> = ManifestParser(context).parse()

    @Inject
    @field:Named("ActivityLifecycle")
    lateinit var mActivityLifecycle: Application.ActivityLifecycleCallbacks

    private var mAppLifecycles: MutableList<AppLifecycles> = ArrayList()

    private var mActivityLifecycles: MutableList<Application.ActivityLifecycleCallbacks> =
            ArrayList()

    init {
        //遍历之前获得的集合, 执行每一个 ConfigModule 实现类的某些方法
        for (mModule in mModules) {
            //将框架外部, 开发者实现的 Application 的生命周期回调 (AppLifecycles) 存入 mAppLifecycles 集合 (此时还未注册回调)
            mModule.injectAppLifecycle(context, mAppLifecycles)

            //将框架外部, 开发者实现的 Activity 的生命周期回调 (ActivityLifecycleCallbacks) 存入 mActivityLifecycles 集合 (此时还未注册回调)
            mModule.injectActivityLifecycle(context, mActivityLifecycles)
        }
    }

    override fun attachBaseContext(base: Context?) {
        //遍历 mAppLifecycles, 执行所有已注册的 AppLifecycles 的 attachBaseContext() 方法
        for (lifecycle in mAppLifecycles) {
            lifecycle.attachBaseContext(base)
        }
    }

    override fun onCreate(application: Application) {
        //多线程防止多次初始化
        if (application.getProcessName() != application.packageName) {
            return
        }
        this.mApplication = application
        MMKV.initialize(application)
//        MMKV.mmkvWithID("ehr", MMKV.MULTI_PROCESS_MODE).encode("bool", true)
        mAppComponent = DaggerAppComponent
                .builder()
                .application(application)
                .globalConfigModule(getGlobalConfigModule(application, mModules))
                .build()
        mAppComponent?.inject(this)

        mAppComponent?.let {
            it.extras().put(IntelligentCache.getKeyOfKeep(ConfigModule::class.java.name), mModules)
        }

        //注册框架内部已实现的 Activity 生命周期逻辑
        application.registerActivityLifecycleCallbacks(mActivityLifecycle)

        //注册框架外部, 开发者扩展的 Activity 生命周期逻辑
        //每个 ConfigModule 的实现类可以声明多个 Activity 的生命周期回调
        //也可以有 N 个 ConfigModule 的实现类 (完美支持组件化项目 各个 Module 的各种独特需求)
        for (lifecycle in mActivityLifecycles) {
            application.registerActivityLifecycleCallbacks(lifecycle)
        }


        //执行框架外部, 开发者扩展的 App onCreate 逻辑
        for (lifecycle in mAppLifecycles) {
            lifecycle.onCreate(application)
        }
    }

    override fun onTerminate(application: Application) {

        mApplication?.unregisterActivityLifecycleCallbacks(mActivityLifecycle)

        if (mActivityLifecycles.size > 0) {
            for (lifecycle in mActivityLifecycles) {
                mApplication?.unregisterActivityLifecycleCallbacks(lifecycle)
            }
        }
        if (mAppLifecycles.size > 0) {
            for (lifecycle in mAppLifecycles) {
                lifecycle.onTerminate(application)
            }
        }
        this.mAppComponent = null
        this.mActivityLifecycles.clear()
        this.mAppLifecycles.clear()
        this.mApplication = null
    }

    override fun getAppComponent(): AppComponent {
        Preconditions.checkNotNull(
                mAppComponent,
                "%s == null, first call %s#onCreate(Application) in %s#onCreate()",
                AppComponent::class.java.name, javaClass.name, if (mApplication == null)
            Application::class.java.name
        else
            mApplication!!.javaClass.name
        )
        return mAppComponent!!
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

        modules.sortByDescending {
            it.mLoadWeight
        }
        //遍历 ConfigModule 集合, 给全局配置 GlobalConfigModule 添加参数
        for (module in modules) {
            module.applyOptions(context, builder)
        }

        return builder.build()
    }

}