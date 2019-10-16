package com.arnold.mvvmcomponent.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arnold.common.architecture.di.component.AppComponent
import com.arnold.common.mvvm.BaseMvvmFragment
import com.arnold.mvvmcomponent.login.di.component.DaggerLoginFragmentComponent
import com.arnold.mvvmcomponent.login.model.LoginFragmentViewModel

class MainFragment : BaseMvvmFragment<LoginFragmentViewModel>() {
    override fun layout(): Any = R.layout.calendar_fragment_main

    override fun initView(view: View, savedInstanceState: Bundle?) {
    }

    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerLoginFragmentComponent
            .builder()
            .appComponent(appComponent)
            .build()
            .inject(this)
    }

    override fun initData(savedInstanceState: Bundle?) {
        mViewModel?.login()
    }

    override fun setData(data: Any) {
    }
}