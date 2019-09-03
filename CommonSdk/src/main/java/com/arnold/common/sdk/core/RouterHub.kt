package com.arnold.common.sdk.core

/**
 * RouterHub 用来定义路由器的路由地址, 以组件名作为前缀, 对每个组件的路由地址进行分组, 可以统一查看和管理所有分组的路由地址
 *
 * 也可以在每个组件内部建立一个私有 RouterHub,
 * 将不需要跨组件的路由地址放入私有 RouterHub 中管理, 只将需要跨组件的路由地址放入基础库的公有 RouterHub 中管理,
 * 如果您不需要集中管理所有路由地址的话,这也是比较推荐的一种方式
 */
interface RouterHub {

    companion object {
        /**
         * 组名
         */
        private const val CALENDAR: String = "/calendar"  //日历组件


        /**
         * 服务组件, 用于给每个组件暴露特有的服务
         */
        private const val SERVICE: String = "/service"


        /**
         * 日历分组
         */
        const val CALENDAR_SERVICE_CALENDARSERVICE: String =  "$CALENDAR$SERVICE/GoldInfoService"
    }


}