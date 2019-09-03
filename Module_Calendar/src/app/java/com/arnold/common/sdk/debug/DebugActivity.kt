package com.arnold.common.sdk.debug

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.arnold.common.architecture.di.component.AppComponent
import com.arnold.common.mvvm.BaseMvvmActivity
import com.arnold.common.repository.utils.DataHelper
import com.arnold.common.sdk.core.GlobalHttpHandlerImpl
import com.arnold.common.sdk.debug.di.component.DaggerDebugActivityComponent
import com.arnold.common.sdk.debug.model.DebugViewModel
import com.arnold.mvvmcomponent.login.R

class DebugActivity : BaseMvvmActivity<DebugViewModel>() {

    override fun layout(): Any = R.layout.activity_debug

    override fun setupActivityComponent(component: AppComponent) {
        DaggerDebugActivityComponent
            .builder()
            .appComponent(component)
            .build()
            .inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        findViewById<TextView>(R.id.debug_tv_host).text = DataHelper.decodeString(GlobalHttpHandlerImpl.BASE_URL_KEY)
    }

    fun login(view: View) {
    }

    fun switchHost(view: View) {

    }

}