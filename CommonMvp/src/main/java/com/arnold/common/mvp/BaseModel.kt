package com.arnold.common.mvp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.arnold.common.architecture.integration.IRepositoryManager

/**
 * @author：tian
 * 创建时间：2018/11/9 19:35
 * 类描述：基类 Model
 * <p>
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
open class BaseModel(var mRepositoryManager: IRepositoryManager?) : IModel, LifecycleObserver {

    override fun onDestroy() {
        mRepositoryManager = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
    }
}