package com.arnold.common.mvp

import com.arnold.common.architecture.base.BaseActivity
import javax.inject.Inject

abstract class BaseMvpActivity<P : IPresenter> : BaseActivity(), IView {

    @Inject
    lateinit var presenter: P


    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

}