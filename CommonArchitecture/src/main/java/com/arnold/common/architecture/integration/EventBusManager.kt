package com.arnold.common.architecture.integration

import org.greenrobot.eventbus.EventBus

class EventBusManager private constructor() {

    /**
     * 注册订阅者
     *
     * @param subscriber 订阅者
     */
    fun register(subscriber: Any) {
        EventBus.getDefault().register(subscriber)
    }

    /**
     * 注销订阅者
     *
     * @param subscriber 订阅者
     */
    fun unregister(subscriber: Any) {
        EventBus.getDefault().unregister(subscriber)
    }


    /**
     * 发送事件
     *
     * @param event 事件
     */
    fun post(event: Any) {

        EventBus.getDefault().post(event)

    }

    /**
     * 发送黏性事件
     *
     * @param event 事件
     */
    fun postSticky(event: Any) {
        EventBus.getDefault().postSticky(event)

    }

    /**
     * 注销黏性事件,
     *
     * @param eventType
     * @param <T>
     * @return
    </T> */
    fun <T> removeStickyEvent(eventType: Class<T>): T? {
        EventBus.getDefault().removeStickyEvent(eventType)
        return null
    }

    /**
     * 清除订阅者和事件的缓存
     */
    fun clear() {
        EventBus.clearCaches()
    }

    companion object {
        @Volatile
        private var instance: EventBusManager? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: EventBusManager().also { instance = it }
            }
    }

}