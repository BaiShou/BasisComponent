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

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.arnold.common.architecture.integration.cache.Cache;
import com.arnold.common.architecture.integration.cache.CacheType;
import com.arnold.common.architecture.utils.Preconditions;

import java.lang.reflect.Proxy;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;
import retrofit2.Retrofit;

@SuppressWarnings("unchecked")
@Singleton
public class RepositoryManager implements IRepositoryManager {

    @Inject
    Lazy<Retrofit> mRetrofit;

    @Inject
    Application mApplication;
    @Inject
    Cache.Factory<String, Object> mCacheFactory;
    @Inject
    @Nullable
    ObtainServiceDelegate mObtainServiceDelegate;
    private Cache<String, Object> mRetrofitServiceCache;
    private Cache<String, Object> mCacheServiceCache;

    @Inject
    public RepositoryManager() {
    }

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     *
     * @param serviceClass ApiService class
     * @param <T>          ApiService class
     * @return ApiService
     */
    @NonNull
    @Override
    public synchronized <T> T obtainRetrofitService(@NonNull Class<T> serviceClass) {
        if (mRetrofitServiceCache == null) {
            mRetrofitServiceCache = mCacheFactory.build(CacheType.RETROFIT_SERVICE_CACHE);
        }
        Preconditions.checkNotNull(mRetrofitServiceCache,
                "Cannot return null from a Cache.Factory#build(int) method");
        T retrofitService = (T) mRetrofitServiceCache.get(serviceClass.getCanonicalName());
        if (retrofitService == null) {
            if (mObtainServiceDelegate != null) {
                retrofitService = mObtainServiceDelegate.createRetrofitService(
                        mRetrofit.get(), serviceClass);
            }
            if (retrofitService == null) {
                retrofitService = (T) Proxy.newProxyInstance(
                        serviceClass.getClassLoader(),
                        new Class[]{serviceClass},
                        new RetrofitServiceProxyHandler(mRetrofit.get(), serviceClass));
            }
            mRetrofitServiceCache.put(serviceClass.getCanonicalName(), retrofitService);
        }
        return retrofitService;
    }

//    /**
//     * 根据传入的 Class 获取对应的 RxCache service
//     *
//     * @param cacheClass Cache class
//     * @param <T>        Cache class
//     * @return Cache
//     */
//    @NonNull
//    @Override
//    public synchronized <T> T obtainCacheService(@NonNull Class<T> cacheClass) {
//        Preconditions.checkNotNull(cacheClass, "cacheClass == null");
//        if (mCacheServiceCache == null) {
//            mCacheServiceCache = mCacheFactory.build(CacheType.CACHE_SERVICE_CACHE);
//        }
//        Preconditions.checkNotNull(mCacheServiceCache,
//                "Cannot return null from a Cache.Factory#build(int) method");
//        T cacheService = (T) mCacheServiceCache.get(cacheClass.getCanonicalName());
//        if (cacheService == null) {
//            cacheService = mRxCache.get().using(cacheClass);
//            mCacheServiceCache.put(cacheClass.getCanonicalName(), cacheService);
//        }
//        return cacheService;
//    }


    @NonNull
    @Override
    public Context getContext() {
        return mApplication;
    }
}
