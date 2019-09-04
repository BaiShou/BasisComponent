package com.arnold.common.architecture.integration

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.arnold.common.architecture.base.IActivity
import com.arnold.common.architecture.base.delegate.ActivityDelegate
import com.arnold.common.architecture.base.delegate.ActivityDelegateImpl
import com.arnold.common.architecture.integration.cache.Cache
import com.arnold.common.architecture.integration.cache.IntelligentCache
import com.arnold.common.architecture.utils.Preconditions
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [Application.ActivityLifecycleCallbacks]  默认实现类
 * 通过 [ActivityDelegate] 管理 [Activity]
 */
@Singleton
class ActivityLifecycle
@Inject
constructor() : Application.ActivityLifecycleCallbacks {

    @Inject
    internal lateinit var mAppManager: AppManager
    @Inject
    internal lateinit var mApplication: Application
    @Inject
    internal lateinit var mExtras: Cache<String, Any>
    @Inject
    internal lateinit var mFragmentLifecycle: Lazy<FragmentManager.FragmentLifecycleCallbacks>
    @Inject
    internal lateinit var mFragmentLifecycles: Lazy<MutableList<FragmentManager.FragmentLifecycleCallbacks>>

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        var isNotAdd: Boolean
        activity.intent.let {
            isNotAdd = it.getBooleanExtra(AppManager.IS_NOT_ADD_ACTIVITY_LIST, false)
        }
        if (!isNotAdd) {
            mAppManager.addActivity(activity)
        }

        if (activity is IActivity) {
            var activityDelegate: ActivityDelegate? = fetchActivityDelegate(activity)
            if (activityDelegate == null) {
                val cache = getCacheFromActivity(activity as IActivity)
                activityDelegate = ActivityDelegateImpl(activity)
                //使用 IntelligentCache.KEY_KEEP 作为 key 的前缀, 可以使储存的数据永久存储在内存中
                //否则存储在 LRU 算法的存储空间中, 前提是 Activity 使用的是 IntelligentCache (框架默认使用)
                cache.put(
                    IntelligentCache.getKeyOfKeep(ActivityDelegate.ACTIVITY_DELEGATE),
                    activityDelegate
                )
            }
            activityDelegate.onCreate(savedInstanceState)
        }

        registerFragmentCallbacks(activity)

    }

    override fun onActivityPaused(activity: Activity) {
        fetchActivityDelegate(activity)?.onPause()
    }

    override fun onActivityStarted(activity: Activity) {
        fetchActivityDelegate(activity)?.onStart()
    }

    override fun onActivityDestroyed(activity: Activity) {
        mAppManager.removeActivity(activity)
        fetchActivityDelegate(activity)?.let {
            it.onDestroy()
            getCacheFromActivity(activity = activity as IActivity).clear()
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, savedInstanceState: Bundle) {
        fetchActivityDelegate(activity)?.onSaveInstanceState(savedInstanceState)
    }

    override fun onActivityStopped(activity: Activity) {
        if (mAppManager.currentActivity == activity) {
            mAppManager.currentActivity = null
        }
        fetchActivityDelegate(activity)?.onStop()
    }


    override fun onActivityResumed(activity: Activity) {
        mAppManager.currentActivity = activity
        fetchActivityDelegate(activity)?.onResume()
    }


    /**
     * 给每个 Activity 的所有 Fragment 设置监听其生命周期, Activity 可以通过 [IActivity.useFragment]
     * 设置是否使用监听,如果这个 Activity 返回 false 的话,这个 Activity 下面的所有 Fragment 将不能使用 [FragmentDelegate]
     * 意味着 [BaseFragment] 也不能使用
     *
     * @param activity
     */
    private fun registerFragmentCallbacks(activity: Activity) {

        val useFragment = if (activity is IActivity) (activity as IActivity).useFragment() else true
        if (activity is FragmentActivity && useFragment) {

            //mFragmentLifecycle 为 Fragment 生命周期实现类, 用于框架内部对每个 Fragment 的必要操作, 如给每个 Fragment 配置 FragmentDelegate
            //注册框架内部已实现的 Fragment 生命周期逻辑
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                mFragmentLifecycle.get(),
                true
            )

            if (mExtras.containsKey(IntelligentCache.getKeyOfKeep(ConfigModule::class.java.name))) {
                val modules =
                    mExtras.get(IntelligentCache.getKeyOfKeep(ConfigModule::class.java.name)) as List<ConfigModule>
                for (module in modules) {
                    module.injectFragmentLifecycle(mApplication, mFragmentLifecycles.get())
                }
                mExtras.remove(IntelligentCache.getKeyOfKeep(ConfigModule::class.java.name))
            }

            //注册框架外部, 开发者扩展的 Fragment 生命周期逻辑
            for (fragmentLifecycle in mFragmentLifecycles.get()) {
                activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                    fragmentLifecycle,
                    true
                )
            }
        }
    }

    private fun fetchActivityDelegate(activity: Activity): ActivityDelegate? {
        var activityDelegate: ActivityDelegate? = null
        if (activity is IActivity) {
            val cache = getCacheFromActivity(activity as IActivity)
            activityDelegate = cache.get(IntelligentCache.getKeyOfKeep(ActivityDelegate.ACTIVITY_DELEGATE)) as ActivityDelegate?
        }
        return activityDelegate
    }

    private fun getCacheFromActivity(activity: IActivity): Cache<String, Any> {
        val cache = activity.provideCache()
        Preconditions.checkNotNull(cache, "%s cannot be null on Activity", Cache::class.java.name)
        return cache
    }

}