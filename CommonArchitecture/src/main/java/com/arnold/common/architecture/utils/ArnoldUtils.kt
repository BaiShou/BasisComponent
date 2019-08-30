package com.arnold.common.architecture.utils

import android.content.Context
import com.arnold.common.architecture.base.App
import com.arnold.common.architecture.di.component.AppComponent
import okhttp3.MediaType

object ArnoldUtils {

    fun obtainAppComponentFromContext(context: Context): AppComponent {
        Preconditions.checkNotNull(context, "%s cannot be null", Context::class.java.name)
        Preconditions.checkState(
            context.applicationContext is App,
            "%s must be implements %s",
            context.applicationContext.javaClass.name,
            App::class.java.getName()
        )
        return (context.applicationContext as App).getAppComponent()
    }

}