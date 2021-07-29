/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.arnold.common.architecture.integration;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import retrofit2.Retrofit;

/**
 * 用来管理网络请求层,以及数据缓存层
 */
public interface IRepositoryManager {

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     *
     * @param service Retrofit service class
     * @param <T>     Retrofit service 类型
     * @return Retrofit service
     */
    @NonNull
    <T> T obtainRetrofitService(@NonNull Class<T> service);


//    /**
//     * 根据传入的 Class 获取对应的 RxCache service
//     *
//     * @param cache RxCache service class
//     * @param <T>   RxCache service 类型
//     * @return RxCache service
//     */
//    @NonNull
//    <T> T obtainCacheService(@NonNull Class<T> cache);


    /**
     * 获取 Context
     *
     * @return Context
     */
    @NonNull
    Context getContext();

    interface ObtainServiceDelegate {

        @Nullable
        <T> T createRetrofitService(Retrofit retrofit, Class<T> serviceClass);
    }
}
