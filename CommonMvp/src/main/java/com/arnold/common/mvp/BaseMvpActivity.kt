package com.arnold.common.mvp

import android.os.Bundle
import com.arnold.common.architecture.base.BaseActivity
import javax.inject.Inject

abstract class BaseMvpActivity<P : IPresenter> : BaseActivity(), IView {

    @Inject
    lateinit var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData(savedInstanceState)
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

}