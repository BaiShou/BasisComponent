package com.arnold.common.mvp

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class BasePresenter<BV : IView, M : IModel>(var mView: BV?, var mModel: M?) :
    IPresenter {

    init {
        onStart()
    }

    val mCompositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    fun unSubscribe() {
        mCompositeDisposable.clear()
    }

    fun addSubscribe(subscription: Disposable) {

        mCompositeDisposable.add(subscription)
    }

    fun removeSubscribe(subscription: Disposable?) {
        subscription?.let {
            mCompositeDisposable.remove(it)
        }
    }


    override fun onStart() {

    }

    override fun onDestroy() {
        unSubscribe()
        mModel?.onDestroy()
        mModel = null
        mView = null
    }

}