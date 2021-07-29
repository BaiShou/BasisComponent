package com.arnold.common.mvp

interface IView {
    /**
     * 显示加载
     */
    fun showLoading(){}

    /**
     * 隐藏加载
     */
    fun hideLoading(){}

    /**
     * 停止加载
     */
    fun stopLoading(){}

    /**
     * 显示信息
     *
     * @param message 消息内容, 不能为 `null`
     */
    fun showMessage(message: String){
    }
}