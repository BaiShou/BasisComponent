package com.arnold.common.repository.di.module

import android.app.Application
import android.content.Context
import com.arnold.common.repository.utils.DataHelper
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import io.rx_cache2.internal.RxCache
import io.victoralbertos.jolyglot.GsonSpeaker
import java.io.File
import javax.inject.Named
import javax.inject.Singleton

@Module
class RepositoryModule {


    /**
     * 提供 [RxCache]
     *
     * @param application    [Application]
     * @param configuration  [RxCacheConfiguration]
     * @param cacheDirectory RxCache 缓存路径
     * @param gson           [Gson]
     * @return [RxCache]
     */
    @Singleton
    @Provides
    internal fun provideRxCache(
        application: Application, configuration: RxCacheConfiguration?,
        @Named("RxCacheDirectory") cacheDirectory: File,
        gson: Gson
    ): RxCache {
        val builder = RxCache.Builder()
        var rxCache: RxCache? = null
        if (configuration != null) {
            rxCache = configuration.configRxCache(application, builder)
        }
        return rxCache ?: builder.persistence(cacheDirectory, GsonSpeaker(gson))
    }

    /**
     * 需要单独给 [RxCache] 提供子缓存文件
     *
     * @param cacheDir 框架缓存文件
     * @return [File]
     */
    @Singleton
    @Provides
    @Named("RxCacheDirectory")
    internal fun provideRxCacheDirectory(cacheDir: File): File {
        val cacheDirectory = File(cacheDir, "RxCache")
        return DataHelper.makeDirs(cacheDirectory)
    }



    /**
     * [RxCache] 自定义配置接口
     */
    interface RxCacheConfiguration {
        /**
         * 若想自定义 RxCache 的缓存文件夹或者解析方式, 如改成 FastJson
         * 请 `return rxCacheBuilder.persistence(cacheDirectory, new FastJsonSpeaker());`, 否则请 `return null;`
         *
         * @param context [Context]
         * @param builder [RxCache.Builder]
         * @return [RxCache]
         */
        fun configRxCache(context: Context, builder: RxCache.Builder): RxCache?
    }
}