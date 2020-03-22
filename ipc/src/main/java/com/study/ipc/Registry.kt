package com.study.ipc

import android.util.Log
import com.study.ipc.annotation.ServiceId
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.thread

/**
 * Date:2020/3/15
 * author:lwb
 * Desc: 负责记录 服务端注册的信息。
 */
class Registry {

    companion object {
        private const val TAG = "Registry"

        private var mInstance: Registry? = null
        fun getInstance(): Registry {
            if (null == mInstance) {
                synchronized(Registry::class.java) {
                    if (null == mInstance) {
                        mInstance = Registry()
                    }
                }
            }
            return mInstance!!
        }
    }

    // 服务表
    private val mServices = ConcurrentHashMap<String, Class<*>>()

    // 方法表
    private val mMethods = ConcurrentHashMap<Class<*>, ConcurrentHashMap<String, Method>>()

    /**
     * 类id：实例对象
     */
    private val mObjects = ConcurrentHashMap<String, Any>()

    /**
     * 做两张表
     * 1、服务表 Class的标记 ：Class<？>
     * 2、方法表 Class<?> : ["getLocation":Method,"setLocation":Method]
     * @param clazz
     */
    fun register(service: Class<*>) {
        // 1.服务的id 与 class的表
        val serviceId = service.getAnnotation(ServiceId::class.java)
        if (null == serviceId) {
            thread { RuntimeException("must use ServiceId Annotation") }
        }
        val value = serviceId?.value ?: ""
        mServices[value] = service

        // 2. class 与 方法的表
        var methods = mMethods[service]
        if (null == methods) {
            methods = ConcurrentHashMap()
            mMethods[service] = methods
        }
        for (method in service.methods) {
            // 因为有重载方法的存在，所以不能以方法名 作为key！ 带上参数
            val builder = StringBuilder(method.name)
            builder.append("(")


            // 方法的参数类型数组
            val parameterTypes = method.parameterTypes
            if (parameterTypes.isNotEmpty()) {
                builder.append(parameterTypes[0].name)
            }
            for (index in 1 until parameterTypes.size) {
                builder.append(",${parameterTypes[index].name}")
            }

            builder.append(")")

            methods[builder.toString()] = method
        }


        val entries: Set<Map.Entry<String, Class<*>>> =
            mServices.entries
        for (entry in entries) {
            Log.e(TAG, "服务表:" + entry.key + " = " + entry.value)
        }

        val entries1 = mMethods.entries
        for (mutableEntry in entries1) {
            val value1 = mutableEntry.value
            Log.e(TAG, "方法表：" + mutableEntry.key)
            for (entry in value1.entries) {
                Log.e(TAG, "" + entry.key)
            }
        }
    }

    /**
     * 查找对应的method
     */
    fun findMethod(objects: Array<Any?>, methodName: String?, serviceId: String?): Method? {
        val service = mServices[serviceId]
        val methods = mMethods[service]
        val sb = StringBuilder(methodName ?: "")
        sb.append("(")
        if (objects.isNotEmpty()) {
            sb.append(objects[0]?.javaClass?.name)
            for (i in 1 until objects.size) {
                sb.append(",${objects[i]?.javaClass?.name}")
            }
        }
        sb.append(")")
        return methods!![sb.toString()]
    }

    fun putInstance(serviceId: String?, instance: Any?) {
        mObjects[serviceId!!] = instance!!
    }

    fun getInstance(serviceId: String?): Any? {
        return mObjects[serviceId]
    }
}