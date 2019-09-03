package com.arnold.mvvmcomponent.login.di.component

import com.arnold.common.architecture.di.component.AppComponent
import com.arnold.common.architecture.di.scope.FragmentScope
import com.arnold.common.mvvm.di.module.ViewModelFactoryModule
import com.arnold.mvvmcomponent.login.MainFragment
import com.arnold.mvvmcomponent.login.di.module.LoginFragmentModule
import dagger.Component

@FragmentScope
@Component(
    dependencies = [AppComponent::class],
    modules = [LoginFragmentModule::class, ViewModelFactoryModule::class]
)
interface LoginFragmentComponent {

    fun inject(fragment: MainFragment)
}