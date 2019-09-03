package com.arnold.mvvmcomponent.login.common.service

import android.content.Context
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.arnold.common.sdk.core.RouterHub
import com.arnold.common.service.calendar.service.ICalendarService
import com.arnold.mvvmcomponent.login.MainFragment

@Route(path = RouterHub.CALENDAR_SERVICE_CALENDARSERVICE)
class CalendarServiceImpl : ICalendarService {
    var context: Context? = null
    override fun getFragment(): Fragment {
        return MainFragment()
    }

    override fun init(context: Context?) {
        this.context = context
    }
}