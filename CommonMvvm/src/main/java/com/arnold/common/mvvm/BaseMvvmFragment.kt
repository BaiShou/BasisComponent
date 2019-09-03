package com.arnold.common.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.arnold.common.architecture.base.BaseFragment
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

abstract class BaseMvvmFragment<VM : BaseViewModel<*>> : BaseFragment() {

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    var mViewModel: VM? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
        initViewModel()
    }

    fun initViewModel() {
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