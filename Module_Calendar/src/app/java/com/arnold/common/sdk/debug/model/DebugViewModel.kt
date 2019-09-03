package com.arnold.common.sdk.debug.model

import android.app.Application
import com.arnold.common.mvvm.BaseViewModel
import javax.inject.Inject

class DebugViewModel
@Inject
constructor(application: Application, debugModel: DebugModel) :
    BaseViewModel<DebugModel>(application, debugModel) {
}