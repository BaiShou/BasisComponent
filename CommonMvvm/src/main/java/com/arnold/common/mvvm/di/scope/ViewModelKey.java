package com.arnold.common.mvvm.di.scope;


import android.arch.lifecycle.ViewModel;

import dagger.MapKey;
import kotlin.annotation.AnnotationTarget;
import kotlin.annotation.MustBeDocumented;
import kotlin.annotation.Retention;
import kotlin.annotation.Target;

@MustBeDocumented
@Target(allowedTargets = AnnotationTarget.FUNCTION)
@Retention()
@MapKey
public @interface ViewModelKey {

    Class<? extends ViewModel> value();
}