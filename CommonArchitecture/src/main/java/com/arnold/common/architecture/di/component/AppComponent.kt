package com.arnold.common.architecture.di.component

import android.app.Application
import com.arnold.common.architecture.base.delegate.AppDelegate
import com.arnold.common.architecture.di.module.AppModule
import com.arnold.common.architecture.di.module.ClientModule
import com.arnold.common.architecture.di.module.GlobalConfigModule
import com.arnold.common.architecture.integration.cache.Cache
import com.google.gson.Gson
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import okhttp3.OkHttpClient
import java.io.File
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class,
        ClientModule::class,
        GlobalConfigModule::class,
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class]
)
interface AppComponent {

    fun application(): Application

    /**
     * 网络请求框架
     *
     * @return [OkHttpClient]
     */
    fun okHttpClient(): OkHttpClient


    /**
     * Json 序列化库
     *
     * @return [Gson]
     */
    fun gson(): Gson

    /**
     * 缓存文件根目录 (RxCache 和 Glide 的缓存都已经作为子文件夹放在这个根目录下), 应该将所有缓存都统一放到这个根目录下
     * 便于管理和清理, 可在 [ConfigModule.applyOptions] 种配置
     *
     * @return [File]
     */
    fun cacheFile(): File


    /**
     * 用来存取一些整个 App 公用的数据, 切勿大量存放大容量数据, 这里的存放的数据和 [Application] 的生命周期一致
     *
     * @return [Cache]
     */
    fun extras(): Cache<String, Any>

    /**
     * 用于创建框架所需缓存对象的工厂
     *
     * @return [Cache.Factory]
     */
    fun cacheFactory(): Cache.Factory<String, Any>

    fun inject(appDelegate: AppDelegate)


    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun globalConfigModule(globalConfigModule: GlobalConfigModule): Builder

        fun build(): AppComponent

    }

}