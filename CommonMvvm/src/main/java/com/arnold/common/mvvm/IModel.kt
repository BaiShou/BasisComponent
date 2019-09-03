package com.arnold.common.mvvm

/**
 * 框架要求框架中的每个 Model 都需要实现此类,以满足规范
 */
interface IModel {

    fun onDestroy()
}