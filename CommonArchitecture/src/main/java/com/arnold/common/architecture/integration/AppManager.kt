package com.arnold.common.architecture.integration

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.app.Service
import android.content.Intent
import android.os.Message
import com.arnold.common.architecture.utils.LogUtil
import java.util.*

open class AppManager private constructor() {
    protected val TAG = this.javaClass.simpleName

    private var mApplication: Application? = null
    /**
     * 管理所有存活的 Activity, 容器中的顺序仅仅是 Activity 的创建顺序, 并不能保证和 Activity 任务栈顺序一致
     */
    private var mActivityList: LinkedList<Activity>? = null
    /**
     * 当前在前台的 Activity
     */
    /**
     * 获取在前台的 [Activity] (保证获取到的 [Activity] 正处于可见状态, 即未调用 [Activity.onStop]), 获取的 [Activity] 存续时间
     * 是在 [Activity.onStop] 之前, 所以如果当此 [Activity] 调用 [Activity.onStop] 方法之后, 没有其他的 [Activity] 回到前台(用户返回桌面或者打开了其他 App 会出现此状况)
     * 这时调用 [.getCurrentActivity] 有可能返回 `null`, 所以请注意使用场景和 [.getTopActivity] 不一样
     *
     *
     * Example usage:
     * 使用场景比较适合, 只需要在可见状态的 [Activity] 上执行的操作
     * 如当后台 [Service] 执行某个任务时, 需要让前台 [Activity] ,做出某种响应操作或其他操作,如弹出 [Dialog], 这时在 [Service] 中就可以使用 [.getCurrentActivity]
     * 如果返回为 `null`, 说明没有前台 [Activity] (用户返回桌面或者打开了其他 App 会出现此状况), 则不做任何操作, 不为 `null`, 则弹出 [Dialog]
     *
     * @return
     */
    /**
     * 将在前台的 [Activity] 赋值给 `currentActivity`, 注意此方法是在 [Activity.onResume] 方法执行时将栈顶的 [Activity] 赋值给 `currentActivity`
     * 所以在栈顶的 [Activity] 执行 [Activity.onCreate] 方法时使用 [.getCurrentActivity] 获取的就不是当前栈顶的 [Activity], 可能是上一个 [Activity]
     * 如果在 App 启动第一个 [Activity] 执行 [Activity.onCreate] 方法时使用 [.getCurrentActivity] 则会出现返回为 `null` 的情况
     * 想避免这种情况请使用 [.getTopActivity]
     *
     * @param currentActivity
     */
    var currentActivity: Activity? = null


    /**
     * 获取最近启动的一个 [Activity], 此方法不保证获取到的 [Activity] 正处于前台可见状态
     * 即使 App 进入后台或在这个 [Activity] 中打开一个之前已经存在的 [Activity], 这时调用此方法
     * 还是会返回这个最近启动的 [Activity], 因此基本不会出现 `null` 的情况
     * 比较适合大部分的使用场景, 如 startActivity
     *
     *
     * Tips: mActivityList 容器中的顺序仅仅是 Activity 的创建顺序, 并不能保证和 Activity 任务栈顺序一致
     *
     * @return
     */
    val topActivity: Activity?
        get() {
            if (mActivityList == null) {
                LogUtil.w("mActivityList == null when getTopActivity()")
                return null
            }
            return if (mActivityList!!.size > 0) mActivityList!![mActivityList!!.size - 1] else null
        }

    /**
     * 返回一个存储所有未销毁的 [Activity] 的集合
     *
     * @return
     */
    val activityList: LinkedList<Activity>
        get() {
            if (mActivityList == null) {
                mActivityList = LinkedList()
            }
            return mActivityList!!
        }

    fun init(application: Application): AppManager{
        this.mApplication = application
        return sAppManager
    }

    /**
     * 让在栈顶的 [Activity] ,打开指定的 [Activity]
     *
     * @param intent
     */
    fun startActivity(intent: Intent) {
        if (topActivity == null) {
            LogUtil.w("mCurrentActivity == null when startActivity(Intent)")
            //如果没有前台的activity就使用new_task模式启动activity
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            mApplication!!.startActivity(intent)
            return
        }
        topActivity!!.startActivity(intent)
    }

    /**
     * 让在栈顶的 [Activity] ,打开指定的 [Activity]
     *
     * @param activityClass
     */
    fun startActivity(activityClass: Class<*>) {
        startActivity(Intent(mApplication, activityClass))
    }

    /**
     * 释放资源
     */
    fun release() {
        mActivityList!!.clear()
        mActivityList = null
        currentActivity = null
        mApplication = null
    }

    /**
     * 添加 [Activity] 到集合
     */
    fun addActivity(activity: Activity) {
        synchronized(AppManager::class.java) {
            val activities = activityList
            if (!activities.contains(activity)) {
                activities.add(activity)
            }
        }
    }

    /**
     * 删除集合里的指定的 [Activity] 实例
     *
     * @param {@link Activity}
     */
    fun removeActivity(activity: Activity) {
        if (mActivityList == null) {
            LogUtil.w("mActivityList == null when removeActivity(Activity)")
            return
        }
        synchronized(AppManager::class.java) {
            if (mActivityList!!.contains(activity)) {
                mActivityList!!.remove(activity)
            }
        }
    }

    /**
     * 删除集合里的指定位置的 [Activity]
     *
     * @param location
     */
    fun removeActivity(location: Int): Activity? {
        if (mActivityList == null) {
            LogUtil.w("mActivityList == null when removeActivity(int)")
            return null
        }
        synchronized(AppManager::class.java) {
            if (location > 0 && location < mActivityList!!.size) {
                return mActivityList!!.removeAt(location)
            }
        }
        return null
    }

    /**
     * 关闭指定的 [Activity] class 的所有的实例
     *
     * @param activityClass
     */
    fun killActivity(activityClass: Class<*>) {
        if (mActivityList == null) {
            LogUtil.w("mActivityList == null when killActivity(Class)")
            return
        }
        synchronized(AppManager::class.java) {
            val iterator = activityList.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()

                if (next.javaClass == activityClass) {
                    iterator.remove()
                    next.finish()
                }
            }
        }
    }

    /**
     * 指定的 [Activity] 实例是否存活
     *
     * @param {@link Activity}
     * @return
     */
    fun activityInstanceIsLive(activity: Activity): Boolean {
        if (mActivityList == null) {
            LogUtil.w("mActivityList == null when activityInstanceIsLive(Activity)")
            return false
        }
        return mActivityList!!.contains(activity)
    }

    /**
     * 指定的 [Activity] class 是否存活(同一个 [Activity] class 可能有多个实例)
     *
     * @param activityClass
     * @return
     */
    fun activityClassIsLive(activityClass: Class<*>): Boolean {
        if (mActivityList == null) {
            LogUtil.w("mActivityList == null when activityClassIsLive(Class)")
            return false
        }
        for (activity in mActivityList!!) {
            if (activity.javaClass == activityClass) {
                return true
            }
        }
        return false
    }

    /**
     * 获取指定 [Activity] class 的实例,没有则返回 null(同一个 [Activity] class 有多个实例,则返回最早创建的实例)
     *
     * @param activityClass
     * @return
     */
    fun findActivity(activityClass: Class<*>): Activity? {
        if (mActivityList == null) {
            LogUtil.w("mActivityList == null when findActivity(Class)")
            return null
        }
        for (activity in mActivityList!!) {
            if (activity.javaClass == activityClass) {
                return activity
            }
        }
        return null
    }

    /**
     * 关闭所有 [Activity]
     */
    fun killAll() {
        //        while (getActivityList().size() != 0) { //此方法只能兼容LinkedList
        //            getActivityList().remove(0).finish();
        //        }
        synchronized(AppManager::class.java) {
            val iterator = activityList.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                iterator.remove()
                next.finish()
            }
        }
    }

    /**
     * 关闭所有 [Activity],排除指定的 [Activity]
     *
     * @param excludeActivityClasses activity class
     */
    fun killAll(vararg excludeActivityClasses: Class<*>) {
        val excludeList = Arrays.asList(*excludeActivityClasses)
        synchronized(AppManager::class.java) {
            val iterator = activityList.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()

                if (excludeList.contains(next.javaClass))
                    continue

                iterator.remove()
                next.finish()
            }
        }
    }

    /**
     * 关闭所有 [Activity],排除指定的 [Activity]
     *
     * @param excludeActivityName [Activity] 的完整全路径
     */
    fun killAll(vararg excludeActivityName: String) {
        val excludeList = Arrays.asList(*excludeActivityName)
        synchronized(AppManager::class.java) {
            val iterator = activityList.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()

                if (excludeList.contains(next.javaClass.name))
                    continue

                iterator.remove()
                next.finish()
            }
        }
    }

    /**
     * 退出应用程序
     *
     *
     * 此方法经测试在某些机型上并不能完全杀死 App 进程, 几乎试过市面上大部分杀死进程的方式, 但都发现没卵用, 所以此
     * 方法如果不能百分之百保证能杀死进程, 就不能贸然调用 [.release] 释放资源, 否则会造成其他问题, 如果您
     * 有测试通过的并能适用于绝大多数机型的杀死进程的方式, 望告知
     */
    fun appExit() {
        try {
            killAll()
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @Deprecated("")
    interface HandleListener {
        fun handleMessage(appManager: AppManager, message: Message)
    }

    companion object {
        val IS_NOT_ADD_ACTIVITY_LIST = "is_not_add_activity_list"
        private val sAppManager: AppManager by lazy { AppManager() }
        fun getAppManager(): AppManager {
            return sAppManager
        }

    }
}