package com.arnold.common.mvp

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v4.app.SupportActivity
import com.trello.rxlifecycle2.RxLifecycle
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


open class BasePresenter<BV : IView, M : IModel>(var mView: BV?, var mModel: M) :
    IPresenter,LifecycleObserver {

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
        mView?.let {view->
            if (view is LifecycleOwner){
                view.lifecycle.addObserver(this)
                if (mModel is LifecycleObserver) {
                    view.lifecycle.addObserver((mModel as LifecycleObserver))
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopLoading() {
        mView?.stopLoading()
    }

    /**
     * 只有当 `mRootView` 不为 null, 并且 `mRootView` 实现了 [LifecycleOwner] 时, 此方法才会被调用
     * 所以当您想在 [Service] 以及一些自定义 [View] 或自定义类中使用 `Presenter` 时
     * 您也将不能继续使用 [OnLifecycleEvent] 绑定生命周期
     *
     * @param owner link [SupportActivity] and [Fragment]
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy(owner: LifecycleOwner) {
        /**
         * 注意, 如果在这里调用了 [.onDestroy] 方法, 会出现某些地方引用 `mModel` 或 `mRootView` 为 null 的情况
         * 比如在 [RxLifecycle] 终止 [Observable] 时, 在 [io.reactivex.Observable.doFinally] 中却引用了 `mRootView` 做一些释放资源的操作, 此时会空指针
         * 或者如果你声明了多个 @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY) 时在其他 @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
         * 中引用了 `mModel` 或 `mRootView` 也可能会出现此情况
         */
        owner.lifecycle.removeObserver(this)
    }

    override fun onDestroy() {
        unSubscribe()
        mView?.hideLoading()
        mModel.onDestroy()
        mView = null
    }

}