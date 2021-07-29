package com.arnold.common.mvvm.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arnold.common.mvvm.data.Resource
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable

fun <T> Observable<T>.subscribeWithLiveData(liveData: MutableLiveData<Resource<T>>) {
    liveData.postValue(Resource.loading())
    this.subscribe(
        {
            liveData.postValue(Resource.success(it))
        },
        {
            liveData.postValue(Resource.error(it))
        }
    )
}


fun <T> Observable<T>.toResourceLiveData(): LiveData<Resource<T>> {
    val mutableLiveData = MutableLiveData<Resource<T>>()
    mutableLiveData.value = Resource.loading()
    subscribe(
        {
            mutableLiveData.postValue(Resource.success(it))
        },
        {
            mutableLiveData.postValue(Resource.error(it))
        }
    )
    return mutableLiveData
}

fun <T> Flowable<T>.toResourceLiveData(): LiveData<Resource<T>> {
    val mutableLiveData = MutableLiveData<Resource<T>>()
    mutableLiveData.value = Resource.loading()
    subscribe(
        {
            mutableLiveData.postValue(Resource.success(it))
        },
        {
            mutableLiveData.postValue(Resource.error(it))
        }
    )
    return mutableLiveData
}