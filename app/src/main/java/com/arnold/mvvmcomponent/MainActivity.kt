package com.arnold.mvvmcomponent

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.arnold.common.architecture.base.BaseActivity
import com.arnold.common.sdk.core.RouterHub
import com.arnold.common.service.calendar.service.ICalendarService
import kotlinx.android.synthetic.main.activity_main.*

@Route(path = "/app/mian")
class MainActivity : BaseActivity() {

    @Autowired(name = RouterHub.CALENDAR_SERVICE_CALENDARSERVICE)
    @JvmField
    var calendarService: ICalendarService? = null

    override fun enableARouterInject(): Boolean = true

    override fun layout(): Any = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        if (calendarService != null) {
            appTvContent.text = "成功加载日历组件"
        } else {
            appTvContent.text = "日历组件加载失败"
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
    }
}
