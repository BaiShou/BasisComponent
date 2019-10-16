package com.arnold.common.architecture.di.module

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.arnold.common.architecture.integration.*
import com.arnold.common.architecture.integration.cache.Cache
import com.arnold.common.architecture.integration.cache.CacheType
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import java.util.ArrayList
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [AppModuleBinds::class])
class AppModule {

    @Singleton
    @Provides
    internal fun provideGson(application: Application, configuration: GsonConfiguration?): Gson {
        val builder = GsonBuilder()
        configuration?.configGson(application, builder)
        return builder.create()
    }

    /**
     * 之前 [AppManager] 使用 Dagger 保证单例, 只能使用 [AppComponent.appManager] 访问
     * 现在直接将 AppManager 独立为单例类, 可以直接通过静态方法 [AppManager.getAppManager] 访问, 更加方便
     * 但为了不影响之前使用 [AppComponent.appManager] 获取 [AppManager] 的项目, 所以暂时保留这种访问方式
     *
     * @param application
     * @return
     */
    @Singleton
    @Provides
    internal fun provideAppManager(application: Application): AppManager {
        return AppManager.getAppManager().init(application)
    }

    @Singleton
    @Provides
    internal fun provideExtras(cacheFactory: Cache.Factory<String, Any>): Cache<String, Any> {
        return cacheFactory.build(CacheType.EXTRAS)
    }


    @Singleton
    @Provides
    internal fun provideFragmentLifecycles(): List<FragmentManager.FragmentLifecycleCallbacks> {
        return ArrayList<FragmentManager.FragmentLifecycleCallbacks>()
    }

    interface GsonConfiguration {
        fun configGson(context: Context, builder: GsonBuilder)
    }
}


@Module
abstract class AppModuleBinds {
    @Binds
    @Named("ActivityLifecycle")
    internal abstract fun bindActivityLifecycle(activityLifecycle: ActivityLifecycle): Application.ActivityLifecycleCallbacks

    @Binds
    internal abstract fun bindFragmentLifecycle(fragmentLifecycle: FragmentLifecycle): FragmentManager.FragmentLifecycleCallbacks

    @Binds
    internal abstract fun bindRepositoryManager(repositoryManager: RepositoryManager): IRepositoryManager

}