package com.arnold.common.architecture.base

import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

abstract class InjectorBaseActivity : BaseActivity() {

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

}