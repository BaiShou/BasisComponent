package com.arnold.common.architecture.base

import com.arnold.common.architecture.di.component.AppComponent

interface App {

    fun setAppComponent(appComponent: AppComponent) {

    }

    fun getAppComponent(): AppComponent
}