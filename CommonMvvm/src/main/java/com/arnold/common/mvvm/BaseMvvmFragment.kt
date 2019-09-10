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
        initViewModel()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun initViewModel() {
        val modelClass: Class<VM>
        val type = javaClass.genericSuperclass
        modelClass = if (type is ParameterizedType) {
            type.actualTypeArguments[1] as Class<VM>
        } else {
            BaseViewModel::class.java as Class<VM>
        }

        mViewModel = if (scopeTOActivity()) {
            ViewModelProviders.of(this.requireActivity(), mViewModelFactory)[modelClass]
        } else {
            ViewModelProviders.of(this, mViewModelFactory)[modelClass]
        }
    }

    /**
     * ViewModel的生命周期是否在activity范围之内
     */
    open fun scopeTOActivity(): Boolean = false

}