package com.arnold.common.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel

/**
 * 所有[AndroidViewModel]基类
 */
open class BaseViewModel<M : IModel>(application: Application) : AndroidViewModel(application) {

    var mModel: M? = null

    constructor(application: Application, mModel: M) : this(application) {
        this.mModel = mModel
    }


}