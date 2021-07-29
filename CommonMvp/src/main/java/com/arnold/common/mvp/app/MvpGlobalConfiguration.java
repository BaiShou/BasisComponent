package com.arnold.common.mvp.app;

import android.app.Application;
import android.content.Context;

import androidx.fragment.app.FragmentManager;

import com.arnold.common.architecture.base.delegate.AppLifecycles;
import com.arnold.common.architecture.di.module.GlobalConfigModule;
import com.arnold.common.architecture.integration.ConfigModule;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MvpGlobalConfiguration implements ConfigModule {
    @Override
    public int getMLoadWeight() {
        return 100;
    }

    @Override
    public void setMLoadWeight(int i) {}

    @Override
    public void applyOptions(@NotNull Context context, @NotNull GlobalConfigModule.Builder builder) {

    }

    @Override
    public void injectAppLifecycle(@NotNull Context context, @NotNull List<AppLifecycles> lifecycles) {
    }

    @Override
    public void injectActivityLifecycle(@NotNull Context context, @NotNull List<Application.ActivityLifecycleCallbacks> lifecycles) {

    }

    @Override
    public void injectFragmentLifecycle(@NotNull Context context, @NotNull List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {

    }
}
