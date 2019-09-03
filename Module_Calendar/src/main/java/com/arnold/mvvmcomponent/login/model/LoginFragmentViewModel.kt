package com.arnold.mvvmcomponent.login.model

import android.app.Application
import com.arnold.common.architecture.di.scope.ActivityScope
import com.arnold.common.architecture.di.scope.FragmentScope
import com.arnold.common.mvvm.BaseViewModel
import javax.inject.Inject

@FragmentScope
class LoginFragmentViewModel
@Inject
constructor(application: Application, loginModel: LoginModel) :
    BaseViewModel<LoginModel>(application, loginModel) {

    init {
        mModel?.let {
            it.login()
        }
    }


    fun login(){

    }

}