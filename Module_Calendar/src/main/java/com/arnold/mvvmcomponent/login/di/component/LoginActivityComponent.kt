package com.arnold.mvvmcomponent.login.di.component

import com.arnold.common.architecture.di.component.AppComponent
import com.arnold.common.architecture.di.scope.ActivityScope
import com.arnold.common.mvvm.di.module.ViewModelFactoryModule
import com.arnold.mvvmcomponent.login.CalendarMainActivity
import com.arnold.mvvmcomponent.login.di.module.LoginActivityModule
import dagger.Component

@ActivityScope
@Component(
    dependencies = [AppComponent::class],
    modules = [LoginActivityModule::class, ViewModelFactoryModule::class]
)
interface LoginActivityComponent {

    fun inject(activity: CalendarMainActivity)
}