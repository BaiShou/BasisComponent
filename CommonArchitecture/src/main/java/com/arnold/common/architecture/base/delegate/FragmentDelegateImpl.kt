package com.arnold.common.architecture.base.delegate

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.arnold.common.architecture.base.IFragment
import com.arnold.common.architecture.extension.obtainAppComponentFromContext
import com.arnold.common.architecture.integration.EventBusManager

/**
 * [FragmentDelegate] 默认实现类
 */
class FragmentDelegateImpl
constructor(
    private var mFragmentManager: FragmentManager?,
    private var mFragment: Fragment?
) : FragmentDelegate {

    private var iFragment: IFragment? = mFragment as IFragment

    override fun onAttach(context: Context) {
        iFragment?.let {
            //            if (it.enableInject()) {
//                //dagger自动注入
//                AndroidSupportInjection.inject(mFragment)
//            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        iFragment?.let {
            if (it.useEventBus()) {
                EventBusManager.getInstance().register(mFragment!!)
            }

            val activity = mFragment!!.activity
            activity?.let { context ->
                it.setupFragmentComponent(context.obtainAppComponentFromContext())
            }
        }


    }

    override fun onCreateView(view: View, savedInstanceState: Bundle?) {

    }

    override fun onActivityCreate(savedInstanceState: Bundle?) {
        iFragment?.let {
            it.initData(savedInstanceState)
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

    override fun onDestroyView() {

    }

    override fun onDestroy() {
        iFragment?.let {
            if (it.useEventBus()) {
                EventBusManager.getInstance().unregister(mFragment!!)
            }
        }
        mFragmentManager = null
        mFragment = null
        iFragment = null
    }

    override fun onDetach() {
    }

    override fun isAdded(): Boolean {
        mFragment?.let {
            return it.isAdded
        }
        return false
    }
}