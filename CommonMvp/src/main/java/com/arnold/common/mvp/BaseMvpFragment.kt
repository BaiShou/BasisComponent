package com.arnold.common.mvp

import com.arnold.common.architecture.base.BaseFragment
import javax.inject.Inject


abstract class BaseMvpFragment<P : IPresenter> : BaseFragment(), IView  {

    @Inject
    lateinit var presenter: P


    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}