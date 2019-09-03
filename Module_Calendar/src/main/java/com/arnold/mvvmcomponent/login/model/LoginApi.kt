package com.arnold.mvvmcomponent.login.model

import com.arnold.common.architecture.http.service.IApi
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginApi : IApi {
    @FormUrlEncoded
    @POST("patriarch/ruleurl/baseinfo")
    fun getRuleUrlBaseInfo(): Observable<String>
}