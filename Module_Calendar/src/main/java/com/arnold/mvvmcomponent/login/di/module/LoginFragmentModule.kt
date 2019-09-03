package com.arnold.mvvmcomponent.login.di.module

import androidx.lifecycle.ViewModel
import com.arnold.common.architecture.integration.IRepositoryManager
import com.arnold.common.mvvm.di.scope.ViewModelKey
import com.arnold.mvvmcomponent.login.model.LoginApi
import com.arnold.mvvmcomponent.login.model.LoginFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [LoginFragmentInternalModule::class])
class LoginFragmentModule {
}

@Module
abstract class LoginFragmentInternalModule {

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
    @ViewModelKey(LoginFragmentViewModel::class)
    abstract fun provideLoginFragmentViewModel(loginViewModel: LoginFragmentViewModel): ViewModel
}