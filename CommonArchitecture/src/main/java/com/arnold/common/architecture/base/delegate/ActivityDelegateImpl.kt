package com.arnold.common.architecture.base.delegate

import android.app.Activity
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.arnold.common.architecture.base.IActivity
import com.arnold.common.architecture.integration.EventBusManager
import com.arnold.common.architecture.utils.ArnoldUtils
import dagger.android.AndroidInjection

/**
 * [ActivityDelegate] 默认实现类
 */
class ActivityDelegateImpl constructor(private var mActivity: Activity?) : ActivityDelegate {

    private var iActivity: IActivity? = mActivity as IActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        iActivity?.let {
            //            if (it.enableInject()) {
//                //dagger自动注入
//                AndroidInjection.inject(mActivity!!)
//            }
            if (it.useEventBus()) {
                EventBusManager.getInstance().register(mActivity!!)
            }

            if (it.enableARouterInject()) {
                ARouter.getInstance().inject(this)
            }

            it.setupActivityComponent(ArnoldUtils.obtainAppComponentFromContext(mActivity!!))
        }
    }


    override fun onStart() {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    override fun onStop() {
    }

    override fun onSaveInstanceState(outState: Bundle) {
    }

    override fun onDestroy() {
        iActivity?.let {
            if (it.useEventBus()) {
                EventBusManager.getInstance().unregister(mActivity!!)
            }
        }
        iActivity = null
        mActivity = null

    }
}