package com.arnold.mvvmcomponent.login.model

import com.arnold.common.mvvm.BaseModel
import io.reactivex.Observable
import javax.inject.Inject

class LoginModel
@Inject
constructor(loginApi: LoginApi) : BaseModel<LoginApi>(loginApi) {

    fun login(): Observable<String> {
        return mApi.getRuleUrlBaseInfo()
    }
}