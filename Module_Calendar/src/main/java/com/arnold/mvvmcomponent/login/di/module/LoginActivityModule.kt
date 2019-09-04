package com.arnold.mvvmcomponent.login.di.module

import android.arch.lifecycle.ViewModel
import com.arnold.common.architecture.integration.IRepositoryManager
import com.arnold.common.mvvm.di.scope.ViewModelKey
import com.arnold.mvvmcomponent.login.model.LoginApi
import com.arnold.mvvmcomponent.login.model.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [LoginActivityInternalModule::class])
class LoginActivityModule {

}


@Module
abstract class LoginActivityInternalModule{
    @Module
    companion object{
        @Provides
        @JvmStatic
        fun provideLoginApi(repositoryManager: IRepositoryManager): LoginApi {
            return repositoryManager.obtainRetrofitService(LoginApi::class.java)
        }
    }


    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun provideLoginViewModel(loginViewModel: LoginViewModel): ViewModel

}


