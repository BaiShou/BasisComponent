package com.arnold.mvvmcomponent.login

import android.os.Bundle
import com.arnold.common.architecture.di.component.AppComponent
import com.arnold.common.mvvm.BaseMvvmActivity
import com.arnold.mvvmcomponent.login.di.component.DaggerLoginActivityComponent
import com.arnold.mvvmcomponent.login.model.LoginViewModel
import com.google.gson.Gson
import javax.inject.Inject

class CalendarMainActivity : BaseMvvmActivity<LoginViewModel>() {

    @Inject
    lateinit var mGson: Gson

    override fun setupActivityComponent(component: AppComponent) {
        DaggerLoginActivityComponent
            .builder()
            .appComponent(component)
            .build()
            .inject(this)
    }

    override fun layout(): Any = R.layout.calendar_activity_main

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun initData(savedInstanceState: Bundle?) {

        mViewModel?.login()
    }

    override fun useEventBus(): Boolean {
        return false
    }
}
