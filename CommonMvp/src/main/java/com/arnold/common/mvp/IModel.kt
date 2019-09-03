package com.arnold.common.mvp

/**
 * @author：tian
 * 创建时间：2018/11/8 21:31
 * 类描述： 框架要求框架中的每个 Model 都需要实现此类,以满足规范
 * <p>
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
interface IModel {
    /**
     * 在框架中 [IPresenter.onDestroy] 时会默认调用 [IModel.onDestroy]
     */
     fun onDestroy()
}