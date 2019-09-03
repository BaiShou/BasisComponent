package com.arnold.common.sdk.debug.di.component

import com.arnold.common.architecture.di.component.AppComponent
import com.arnold.common.architecture.di.scope.ActivityScope
import com.arnold.common.mvvm.di.module.ViewModelFactoryModule
import com.arnold.common.sdk.debug.DebugActivity
import com.arnold.common.sdk.debug.di.module.DebugActivityModule
import dagger.Component

@ActivityScope
@Component(
    dependencies = [AppComponent::class],
    modules = [DebugActivityModule::class, ViewModelFactoryModule::class]
)
interface DebugActivityComponent {

    fun inject(activity: DebugActivity)

    @Component.Builder
    interface Builder {

        fun appComponent(appComponent: AppComponent): Builder

        fun build(): DebugActivityComponent

    }
}