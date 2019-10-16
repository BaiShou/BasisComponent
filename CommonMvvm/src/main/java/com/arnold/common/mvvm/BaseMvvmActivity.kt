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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //初始化ViewModel
        initViewModel()
        initData(savedInstanceState)
    }

    protected fun initViewModel() {

        val modelClass: Class<VM>
        val type = javaClass.genericSuperclass
        modelClass = if (type is ParameterizedType) {
            type.actualTypeArguments[0] as Class<VM>
        } else {
            BaseViewModel::class.java as Class<VM>
        }

        mViewModel = ViewModelProviders.of(this, mViewModelFactory)[modelClass]
    }

}