package com.arnold.common.architecture.integration.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.trello.rxlifecycle2.android.ActivityEvent
import dagger.Lazy
import io.reactivex.subjects.Subject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 配合 [ActivityLifecycleable] 使用,使 [Activity] 具有 [RxLifecycle] 的特性
 */
@Singleton
class ActivityLifecycleForRxLifecycle
@Inject
constructor() : Application.ActivityLifecycleCallbacks {

    @Inject
    internal lateinit var mFragmentLifecycle: Lazy<FragmentLifecycleForRxLifecycle>


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity is ActivityLifecycleable) {
            obtainSubject(activity).onNext(ActivityEvent.CREATE)
            if (activity is FragmentActivity) {
                (activity as FragmentActivity).supportFragmentManager.registerFragmentLifecycleCallbacks(
                    mFragmentLifecycle.get(), true
                )
            }
        }
    }

    override fun onActivityStarted(activity: Activity) {
        if (activity is ActivityLifecycleable) {
            obtainSubject(activity).onNext(ActivityEvent.START)
        }
    }

    override fun onActivityResumed(activity: Activity) {
        if (activity is ActivityLifecycleable) {
            obtainSubject(activity).onNext(ActivityEvent.RESUME)
        }
    }

    override fun onActivityPaused(activity: Activity) {
        if (activity is ActivityLifecycleable) {
            obtainSubject(activity).onNext(ActivityEvent.PAUSE)
        }
    }

    override fun onActivityStopped(activity: Activity) {
        if (activity is ActivityLifecycleable) {
            obtainSubject(activity).onNext(ActivityEvent.STOP)
        }
    }


    override fun onActivitySaveInstanceState(activity: Activity, savedInstanceState: Bundle) {

    }


    override fun onActivityDestroyed(activity: Activity) {
        if (activity is ActivityLifecycleable) {
            obtainSubject(activity).onNext(ActivityEvent.DESTROY)
        }
    }


    /**
     * 从 [BaseActivity] 中获得桥梁对象 `BehaviorSubject<ActivityEvent> mLifecycleSubject`
     *
     */
    private fun obtainSubject(activity: Activity): Subject<ActivityEvent> {
        return (activity as ActivityLifecycleable).provideLifecycleSubject()
    }

}