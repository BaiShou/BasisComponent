package com.arnold.common.sdk.core

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.arnold.common.architecture.base.IActivity

class ActivityLifecycleCallbacksImpl: Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity is IActivity) {
            if (activity.enableARouterInject()) {
                ARouter.getInstance().inject(activity)
            }
        }
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }


    override fun onActivitySaveInstanceState(activity: Activity, savedInstanceState: Bundle) {

    }


    override fun onActivityDestroyed(activity: Activity) {

    }
}