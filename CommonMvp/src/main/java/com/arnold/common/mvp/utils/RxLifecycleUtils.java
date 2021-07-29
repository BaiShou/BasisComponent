package com.arnold.common.mvp.utils;


import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.arnold.common.architecture.utils.Preconditions;
import com.arnold.common.mvp.IView;

import autodispose2.AutoDispose;
import autodispose2.AutoDisposeConverter;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;


/**
 * 使用此类操作 RxLifecycle 的特性
 */
public class RxLifecycleUtils {

    private RxLifecycleUtils() {
        throw new IllegalStateException("you can't instantiate me!");
    }


    /**
     * 绑定 Activity/Fragment 的指定生命周期
     *
     * @param view
     * @param event
     * @param <T>
     * @return
     */
    public static <T> AutoDisposeConverter<T> bindUntilEvent(@NonNull final IView view,
                                                             final Lifecycle.Event event) {
        Preconditions.checkNotNull(view, "view == null");
        if (view instanceof LifecycleOwner) {
            return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from((LifecycleOwner) view, event));
        } else {
            throw new IllegalArgumentException("view isn't LifecycleOwner");
        }
    }

    /**
     * 绑定 Activity/Fragment 的生命周期
     *
     * @param view
     * @param <T>
     * @return
     */
    public static <T> AutoDisposeConverter<T> bindToLifecycle(@NonNull IView view) {
        Preconditions.checkNotNull(view, "view == null");
        if (view instanceof LifecycleOwner) {
            return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from((LifecycleOwner) view));
        } else {
            throw new IllegalArgumentException("view isn't LifecycleOwner");
        }
    }

}
