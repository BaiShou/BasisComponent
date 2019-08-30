package com.arnold.mvvmcomponent.login

import android.os.Bundle
import com.arnold.common.architecture.base.InjectorBaseActivity
import com.arnold.common.architecture.di.component.AppComponent
import com.arnold.common.architecture.utils.LogUtil
import com.arnold.mvvmcomponent.login.di.component.DaggerLoginActivityComponent
import com.google.gson.Gson
import javax.inject.Inject

class MainActivity : InjectorBaseActivity() {

    @Inject
    lateinit var mGson: Gson


    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerLoginActivityComponent
            .builder()
            .appComponent(appComponent)
            .build()
            .inject(this)
    }


    override fun initView(savedInstanceState: Bundle?): Int {
        LogUtil.i(mGson.toString())
        return R.layout.activity_main
    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun useEventBus(): Boolean {
        return false
    }
}
