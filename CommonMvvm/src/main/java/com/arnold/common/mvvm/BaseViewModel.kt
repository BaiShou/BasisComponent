package com.arnold.common.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel

/**
 * 所有[AndroidViewModel]基类
 */
open class BaseViewModel<M : IModel>(application: Application, val mModel: M) : AndroidViewModel(application) {

}