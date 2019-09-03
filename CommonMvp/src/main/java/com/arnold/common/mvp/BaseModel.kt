package com.arnold.common.mvp

import com.arnold.common.architecture.integration.IRepositoryManager

/**
 * @author：tian
 * 创建时间：2018/11/9 19:35
 * 类描述：基类 Model
 * <p>
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
class BaseModel(private var mRepositoryManager: IRepositoryManager?) : IModel {

    override fun onDestroy() {
        mRepositoryManager = null
    }
}