package com.arnold.common.architecture.di.module

import android.app.Application
import android.content.Context
import androidx.annotation.Nullable
import com.arnold.common.architecture.http.GlobalHttpHandler
import com.arnold.common.architecture.http.converter.CustomGsonConverterFactory
import com.arnold.common.architecture.http.log.HttpLogger
import com.arnold.common.architecture.utils.DataHelper
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import io.rx_cache2.internal.RxCache
import io.victoralbertos.jolyglot.GsonSpeaker
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class ClientModule {

    companion object {
        private const val TIME_OUT = 10
    }

    /**
     * 提供 [Retrofit]
     *
     * @param application   [Application]
     * @param configuration [RetrofitConfiguration]
     * @param builder       [Retrofit.Builder]
     * @param client        [OkHttpClient]
     * @param httpUrl       [HttpUrl]
     * @param gson          [Gson]
     * @return [Retrofit]
     */
    @Singleton
    @Provides
    internal fun provideRetrofit(
        application: Application,
        configuration: RetrofitConfiguration?,
        handler: GlobalHttpHandler,
        builder: Retrofit.Builder,
        client: OkHttpClient,
        httpUrl: HttpUrl,
        gson: Gson
    ): Retrofit {
        builder
            .baseUrl(httpUrl)//域名
            .client(client)//设置 OkHttp

        builder
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//使用 RxJava
            .addConverterFactory(CustomGsonConverterFactory.create(gson, handler))//使用 Gson

        configuration?.let {
            it.configRetrofit(application, builder)
        }
        return builder.build()
    }

    /**
     * 提供 [OkHttpClient]
     *
     * @param application     [Application]
     * @param configuration   [OkhttpConfiguration]
     * @param builder         [OkHttpClient.Builder]
     * @param interceptors    [<]
     * @param handler         [GlobalHttpHandler]
     * @param executorService [ExecutorService]
     * @return [OkHttpClient]
     */
    @Singleton
    @Provides
    internal fun provideClient(
        application: Application,
        configuration: OkhttpConfiguration?,
        builder: OkHttpClient.Builder,
        handler: GlobalHttpHandler?,
        executorService: ExecutorService,
        interceptors: MutableList<Interceptor>?
    ): OkHttpClient {


        builder.connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
            .addNetworkInterceptor(HttpLoggingInterceptor(HttpLogger()))

        if (handler != null) {
            builder.addInterceptor { chain ->
                chain.proceed(
                    handler.onHttpRequestBefore(
                        chain,
                        chain.request()
                    )
                )
            }
            //这里还有一个TOKEN处理
        }
        //如果外部提供了 Interceptor 的集合则遍历添加
        if (interceptors != null) {
            for (interceptor in interceptors) {
                builder.addInterceptor(interceptor)
            }
        }

        //为 OkHttp 设置默认的线程池
        builder.dispatcher(Dispatcher(executorService))

        configuration?.configOkhttp(application, builder)
        return builder.build()
    }

    @Singleton
    @Provides
    internal fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
    }

    @Singleton
    @Provides
    internal fun provideClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
    }

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
        application: Application, @Nullable configuration: RxCacheConfiguration?,
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
     * [Retrofit] 自定义配置接口
     */
    interface RetrofitConfiguration {
        fun configRetrofit(context: Context, builder: Retrofit.Builder)
    }

    /**
     * [OkHttpClient] 自定义配置接口
     */
    interface OkhttpConfiguration {
        fun configOkhttp(context: Context, builder: OkHttpClient.Builder)
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