package com.arnold.common.architecture.integration.lifecycle

import com.trello.rxlifecycle2.android.FragmentEvent

/**
 * 让 [Fragment] 实现此接口,即可正常使用 [RxLifecycle]
 */
interface FragmentLifecycleable : Lifecycleable<FragmentEvent> {
}