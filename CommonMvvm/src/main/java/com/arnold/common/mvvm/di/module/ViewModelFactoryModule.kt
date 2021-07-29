package com.arnold.common.mvvm.di.module

import androidx.lifecycle.ViewModelProvider
import com.arnold.common.mvvm.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}