package com.study.ipc

import com.google.gson.Gson
import com.study.ipc.model.Request
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * Date:2020/3/23
 * author:lwb
 * Desc:
 */
class IPCInvocationHandler(
    private val instanceClass: Class<*>,
    private val service: Class<out IPCService>
) : InvocationHandler {

    companion object {
        val gson = Gson()
    }


    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        /**
         * 请求服务端执行对应的方法。
         */
        val response = Channel.getInstance()
            .send(Request.GET_METHOD, service, instanceClass, method?.name, args)

        if (response.isSuccess) {
            val returnType = method?.returnType
            //不是返回null
            if (returnType != Void::class.java && returnType != Void.TYPE) {
                //获取Location的json字符
                val source: String? = response.source
                //反序列化 回 Location
                return gson.fromJson(source, returnType)
            }
        }
        return null
    }
}