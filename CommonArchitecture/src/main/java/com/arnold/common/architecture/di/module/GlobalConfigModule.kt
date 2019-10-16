package com.arnold.common.architecture.di.module

import android.app.Application
import android.text.TextUtils
import com.arnold.common.architecture.integration.ConfigModule
import com.arnold.common.architecture.integration.IRepositoryManager
import com.arnold.common.architecture.integration.cache.Cache
import com.arnold.common.architecture.integration.cache.CacheType
import com.arnold.common.architecture.integration.cache.IntelligentCache
import com.arnold.common.architecture.integration.cache.LruCache
import com.arnold.common.architecture.utils.Preconditions
import com.arnold.common.network.di.module.ClientModule
import com.arnold.common.network.http.BaseUrl
import com.arnold.common.network.http.GlobalHttpHandler
import com.arnold.common.repository.di.module.RepositoryModule
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.internal.threadFactory
import java.io.File
import java.util.concurrent.*
import javax.inject.Singleton


/**
 * 后期看能不能使用with进行修改
 * https://juejin.im/post/59f17a5b51882549fc51412e
 */
@Module
class GlobalConfigModule private constructor(builder: Builder) {
    private val mApiUrl: HttpUrl?
    private val mBaseUrl: BaseUrl?
    private val mHandler: GlobalHttpHandler?
    private val mInterceptors: MutableList<Interceptor>
    private val mCacheFile: File?
    private val mRetrofitConfiguration: ClientModule.RetrofitConfiguration?
    private val mOkhttpConfiguration: ClientModule.OkhttpConfiguration?
    private val mRxCacheConfiguration: RepositoryModule.RxCacheConfiguration?
    private val mGsonConfiguration: AppModule.GsonConfiguration?
    private val mCacheFactory: Cache.Factory<String, Any>?
    private val mExecutorService: ExecutorService?
    private val mModules: MutableList<ConfigModule>
    private val mObtainServiceDelegate: IRepositoryManager.ObtainServiceDelegate?

    init {
        this.mApiUrl = builder.apiUrl
        this.mBaseUrl = builder.baseUrl

        this.mHandler = builder.handler
        this.mInterceptors = builder.interceptors
        this.mModules = builder.modules
        this.mCacheFile = builder.cacheFile
        this.mRetrofitConfiguration = builder.retrofitConfiguration
        this.mOkhttpConfiguration = builder.okhttpConfiguration
        this.mRxCacheConfiguration = builder.rxCacheConfiguration
        this.mGsonConfiguration = builder.gsonConfiguration

        this.mCacheFactory = builder.cacheFactory
        this.mExecutorService = builder.executorService
        this.mObtainServiceDelegate = builder.obtainServiceDelegate;
    }

    /**
     * 提供 BaseUrl,默认使用 <"https://api.github.com/">
     *
     * @return
     */
    @Singleton
    @Provides
    internal fun provideBaseUrl(): HttpUrl? {

        mBaseUrl?.let {
            return it.url()
        }

        val parse = "https://api.github.com/".toHttpUrlOrNull()
        return mApiUrl ?: parse
    }


    @Singleton
    @Provides
    internal fun provideInterceptors(): MutableList<Interceptor> {
        return mInterceptors
    }

    @Singleton
    @Provides
    internal fun provideConfigModules(): MutableList<ConfigModule> {
        return mModules
    }

    /**
     * 提供处理 Http 请求和响应结果的处理类
     *
     * @return
     */
    @Singleton
    @Provides
    internal fun provideGlobalHttpHandler(): GlobalHttpHandler? {
        return mHandler
    }

    /**
     * 提供缓存文件
     */
    @Singleton
    @Provides
    internal fun provideCacheFile(application: Application): File {
        return mCacheFile ?: com.arnold.common.repository.utils.DataHelper.getCacheFile(application)
    }

    @Singleton
    @Provides

    internal fun provideRetrofitConfiguration(): ClientModule.RetrofitConfiguration? {
        return mRetrofitConfiguration
    }

    @Singleton
    @Provides
    internal fun provideOkhttpConfiguration(): ClientModule.OkhttpConfiguration? {
        return mOkhttpConfiguration
    }

    @Singleton
    @Provides
    internal fun provideRxCacheConfiguration(): RepositoryModule.RxCacheConfiguration? {
        return mRxCacheConfiguration
    }

    @Singleton
    @Provides
    internal fun provideGsonConfiguration(): AppModule.GsonConfiguration? {
        return mGsonConfiguration
    }


    @Singleton
    @Provides
    internal fun provideCacheFactory(application: Application): Cache.Factory<String, Any> {
        return mCacheFactory ?: Cache.Factory<String, Any> { type ->
            //若想自定义 LruCache 的 size, 或者不想使用 LruCache, 想使用自己自定义的策略
            //使用 GlobalConfigModule.Builder#cacheFactory() 即可扩展
            when (type.cacheTypeId) {
                //Activity、Fragment 以及 Extras 使用 IntelligentCache (具有 LruCache 和 可永久存储数据的 Map)
                CacheType.EXTRAS_TYPE_ID,
                CacheType.ACTIVITY_CACHE_TYPE_ID,
                CacheType.FRAGMENT_CACHE_TYPE_ID ->
                    IntelligentCache(
                        type.calculateCacheSize(application)
                    )
                //其余使用 LruCache (当达到最大容量时可根据 LRU 算法抛弃不合规数据)
                else -> LruCache(type.calculateCacheSize(application))
            }
        }
    }

    /**
     * 返回一个全局公用的线程池,适用于大多数异步需求。
     * 避免多个线程池创建带来的资源消耗。
     *
     * @return [Executor]
     */
    @Singleton
    @Provides
    internal fun provideExecutorService(): ExecutorService {
        return mExecutorService ?: ThreadPoolExecutor(
            0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
            SynchronousQueue(), threadFactory("arnold Executor", false)
        )
    }

    @Singleton
    @Provides
    fun provideObtainServiceDelegate(): IRepositoryManager.ObtainServiceDelegate? {
        return mObtainServiceDelegate
    }

    class Builder {
        var apiUrl: HttpUrl? = null
        var baseUrl: BaseUrl? = null
        var handler: GlobalHttpHandler? = null
        var interceptors: MutableList<Interceptor> = mutableListOf()
        var modules: MutableList<ConfigModule> = mutableListOf()
        var cacheFile: File? = null
        var retrofitConfiguration: ClientModule.RetrofitConfiguration? = null
        var okhttpConfiguration: ClientModule.OkhttpConfiguration? = null
        var rxCacheConfiguration: RepositoryModule.RxCacheConfiguration? = null
        var gsonConfiguration: AppModule.GsonConfiguration? = null

        var cacheFactory: Cache.Factory<String, Any>? = null
        var executorService: ExecutorService? = null
        var obtainServiceDelegate: IRepositoryManager.ObtainServiceDelegate? = null

        fun baseurl(baseUrl: String): Builder {//基础url
            if (TextUtils.isEmpty(baseUrl)) {
                throw NullPointerException("BaseUrl can not be empty")
            }
            this.apiUrl = baseUrl.toHttpUrlOrNull()
            return this
        }

        fun baseurl(baseUrl: BaseUrl): Builder {
            this.baseUrl = Preconditions.checkNotNull(
                baseUrl,
                BaseUrl::class.java.canonicalName!! + "can not be null."
            )
            return this
        }

        fun configModules(configModules: MutableList<ConfigModule>) {
            this.modules = configModules
        }


        fun globalHttpHandler(handler: GlobalHttpHandler): Builder {//用来处理http响应结果
            this.handler = handler
            return this
        }

        fun addInterceptor(interceptor: Interceptor): Builder {//动态添加任意个interceptor
            this.interceptors.add(interceptor)
            return this
        }

        fun cacheFile(cacheFile: File): Builder {
            this.cacheFile = cacheFile
            return this
        }

        fun retrofitConfiguration(retrofitConfiguration: ClientModule.RetrofitConfiguration): Builder {
            this.retrofitConfiguration = retrofitConfiguration
            return this
        }

        fun okhttpConfiguration(okhttpConfiguration: ClientModule.OkhttpConfiguration): Builder {
            this.okhttpConfiguration = okhttpConfiguration
            return this
        }

        fun rxCacheConfiguration(rxCacheConfiguration: RepositoryModule.RxCacheConfiguration): Builder {
            this.rxCacheConfiguration = rxCacheConfiguration
            return this
        }

        fun gsonConfiguration(gsonConfiguration: AppModule.GsonConfiguration): Builder {
            this.gsonConfiguration = gsonConfiguration
            return this
        }


        fun cacheFactory(cacheFactory: Cache.Factory<String, Any>): Builder {
            this.cacheFactory = cacheFactory
            return this
        }

        fun executorService(executorService: ExecutorService): Builder {
            this.executorService = executorService
            return this
        }

        fun obtainServiceDelegate(obtainServiceDelegate: IRepositoryManager.ObtainServiceDelegate): Builder {
            this.obtainServiceDelegate = obtainServiceDelegate
            return this
        }

        fun build(): GlobalConfigModule {
            return GlobalConfigModule(this)
        }
    }

}
