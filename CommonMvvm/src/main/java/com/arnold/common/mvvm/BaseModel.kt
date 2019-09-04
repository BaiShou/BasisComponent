package com.arnold.common.mvvm

import com.arnold.common.architecture.integration.IRepositoryManager

open class BaseModel(var mRepositoryManager: IRepositoryManager?) : IModel {

    override fun onDestroy() {
        mRepositoryManager = null
    }

}