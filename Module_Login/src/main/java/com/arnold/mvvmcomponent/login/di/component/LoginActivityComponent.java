package com.arnold.mvvmcomponent.login.di.component;

import com.arnold.common.architecture.di.component.AppComponent;
import com.arnold.common.architecture.di.scope.ActivityScope;
import com.arnold.mvvmcomponent.login.MainActivity;

import dagger.Component;

/**
 * @author baisoo
 */
@ActivityScope
@Component(dependencies = AppComponent.class)
public interface LoginActivityComponent {

    void inject(MainActivity activity);
}
