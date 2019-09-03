package com.arnold.common.sdk.debug.model

import com.arnold.common.mvvm.BaseModel
import com.arnold.common.sdk.debug.model.api.DebugApi
import javax.inject.Inject

class DebugModel @Inject constructor(debugApi: DebugApi) : BaseModel<DebugApi>(debugApi) {


}