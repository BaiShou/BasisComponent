package com.arnold.mvvmcomponent.login.model

import com.arnold.common.architecture.integration.IRepositoryManager
import com.arnold.common.mvvm.BaseModel
import io.reactivex.Observable
import javax.inject.Inject

class LoginModel
@Inject
constructor(repositoryManager: IRepositoryManager, private val loginApi: LoginApi) :
    BaseModel(repositoryManager) {

    fun login(): Observable<String> {
        return loginApi.getRuleUrlBaseInfo()
    }
}