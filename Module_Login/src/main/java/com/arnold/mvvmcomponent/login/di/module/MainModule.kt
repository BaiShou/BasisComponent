package com.arnold.mvvmcomponent.login.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import com.arnold.common.architecture.di.scope.ActivityScope
import com.arnold.mvvmcomponent.login.MainActivity


@Module
abstract class MainModule {

    @ActivityScope
    @ContributesAndroidInjector()
    internal abstract fun contributeMainActivity(): MainActivity

}
