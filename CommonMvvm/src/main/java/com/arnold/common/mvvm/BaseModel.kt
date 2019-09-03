package com.arnold.common.mvvm

import com.arnold.common.architecture.http.service.IApi


open class BaseModel<T : IApi> (var mApi: T) : IModel {

    override fun onDestroy() {

    }

}