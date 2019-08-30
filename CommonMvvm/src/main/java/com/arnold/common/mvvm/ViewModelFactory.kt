package com.arnold.common.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider


class ViewModelFactory
@Inject
constructor(val creators: Map<Class<out ViewModel>, Provider<ViewModel>>) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        var viewModelProvider = creators.get(modelClass)
        if (viewModelProvider == null) {
            for ((key, value) in creators) {
                if (modelClass.isAssignableFrom(key)) {
                    viewModelProvider = value
                    break
                }
            }
        }

        viewModelProvider?.let {
            return it.get() as T
        }

        throw NullPointerException("can not find provider for $modelClass")
    }
}