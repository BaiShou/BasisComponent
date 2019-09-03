package com.arnold.common.sdk.core

import android.app.Application
import android.content.Context
import android.text.TextUtils
import androidx.fragment.app.FragmentManager
import com.arnold.common.architecture.base.delegate.AppLifecycles
import com.arnold.common.architecture.di.module.AppModule
import com.arnold.common.architecture.di.module.GlobalConfigModule
import com.arnold.common.architecture.integration.ConfigModule
import okhttp3.OkHttpClient
import com.google.gson.GsonBuilder
import io.rx_cache2.internal.RxCache
import com.alibaba.android.arouter.launcher.ARouter
import com.arnold.common.architecture.utils.LogUtil
import com.arnold.common.network.di.module.ClientModule
import com.arnold.common.repository.di.module.RepositoryModule
import com.arnold.common.repository.utils.DataHelper
import com.arnold.common.sdk.BuildConfig

/**
 * 所有模块的公共初始化配置文件
 */
class GlobalConfiguration : ConfigModule {
    override var mLoadWeight: Int = 10

    override fun applyOptions(context: Context, builder: GlobalConfigModule.Builder) {

        val decodeString = DataHelper.decodeString(GlobalHttpHandlerImpl.BASE_URL_KEY)
        if (TextUtils.isEmpty(decodeString)) {
            DataHelper.encode(GlobalHttpHandlerImpl.BASE_URL_KEY, BuildConfig.api_host)
        }

        builder.baseurl(BuildConfig.api_host)
            .globalHttpHandler(GlobalHttpHandlerImpl(context))
            .gsonConfiguration(object : AppModule.GsonConfiguration {
                override fun configGson(context: Context, builder: GsonBuilder) {
                    //这里可以自己自定义配置Gson的参数
                }
            })
            .okhttpConfiguration(object : ClientModule.OkhttpConfiguration {
                override fun configOkhttp(context: Context, builder: OkHttpClient.Builder) {

                }
            })
            .rxCacheConfiguration(object : RepositoryModule.RxCacheConfiguration {
                override fun configRxCache(context: Context, builder: RxCache.Builder): RxCache? {
                    //这里可以自己自定义配置RxCache的参数
                    builder.useExpiredDataIfLoaderNotAvailable(true)
                    return null
                }
            })
    }

    override fun injectAppLifecycle(context: Context, lifecycles: MutableList<AppLifecycles>) {

        lifecycles.add(object : AppLifecycles {
            override fun attachBaseContext(base: Context?) {

            }

            override fun onCreate(application: Application) {
                if (BuildConfig.LOG_DEBUG) {//Timber日志打印
                    LogUtil.init(true)
                    ARouter.openLog()     // 打印日志
                    ARouter.openDebug()   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
                } else {
                    LogUtil.init(false)
                }
                ARouter.init(application) // 尽可能早,推荐在Application中初始化
            }

            override fun onTerminate(application: Application) {
            }
        })
    }

    override fun injectActivityLifecycle(
        context: Context,
        lifecycles: MutableList<Application.ActivityLifecycleCallbacks>
    ) {
        lifecycles.add(ActivityLifecycleCallbacksImpl())
    }

    override fun injectFragmentLifecycle(
        context: Context,
        lifecycles: MutableList<FragmentManager.FragmentLifecycleCallbacks>
    ) {
        lifecycles.add(FragmentLifecycleCallbacksImpl())
    }
}