package com.arnold.common.sdk.debug.di.module

import androidx.lifecycle.ViewModel
import com.arnold.common.architecture.integration.IRepositoryManager
import com.arnold.common.mvvm.di.scope.ViewModelKey
import com.arnold.common.sdk.debug.model.DebugViewModel
import com.arnold.common.sdk.debug.model.api.DebugApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [DebugActivityInternalModule::class])
class DebugActivityModule {

}


@Module
abstract class DebugActivityInternalModule {
    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideDebugApi(repositoryManager: IRepositoryManager): DebugApi {
            return repositoryManager.obtainRetrofitService(DebugApi::class.java)
        }
    }


    @Binds
    @IntoMap
    @ViewModelKey(DebugViewModel::class)
    abstract fun provideDebugViewModel(loginViewModel: DebugViewModel): ViewModel

}