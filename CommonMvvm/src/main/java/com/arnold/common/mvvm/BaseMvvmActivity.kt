package com.arnold.common.mvvm

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.arnold.common.architecture.base.BaseActivity
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

abstract class BaseMvvmActivity<VM : BaseViewModel<*>> : BaseActivity() {

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    var mViewModel: VM? = null

    override

    fun initData(savedInstanceState: Bundle?) {
        //初始化ViewModel
        initViewModel()
    }

    protected fun initViewModel() {

        val modelClass: Class<VM>
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            modelClass = type.actualTypeArguments[1] as Class<VM>
        } else {
            modelClass = BaseViewModel::class.java as Class<VM>
        }

        mViewModel = ViewModelProviders.of(this, mViewModelFactory)[modelClass]
    }

}