package com.study.ipc

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.gson.Gson
import com.study.ipc.model.Parameters
import com.study.ipc.model.Request
import com.study.ipc.model.Response
import java.lang.Exception


/**
 * Date:2020/3/15
 * author:lwb
 * Desc:
 */
abstract class IPCService : Service() {

    companion object {
        val gson = Gson()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return object : IIPCService.Stub() {
            override fun send(request: Request?): Response {
                val methodName = request?.methodName
                val parameters = request?.parameters
                val serviceId = request?.serviceId
                val objects: Array<Any?> = restoreParameters(parameters)
                val method = Registry.getInstance().findMethod(objects, methodName, serviceId)

                return when (request?.type) {
                    //执行单利方法 静态方法
                    Request.GET_INSTANCE -> {
                        var resout: Response? = null
                        try {
                            val instance = method?.invoke(null, *objects)
                            Registry.getInstance().putInstance(serviceId, instance)
                            Response(null, true)
                        } catch (e: Exception) {
                            Response(null, false)
                            e.printStackTrace()
                        }
                        resout!!
                    }

                    //执行普通方法
                    Request.GET_METHOD -> {
                        val instance = Registry.getInstance().getInstance(serviceId)
                        val invoke = method?.invoke(instance, *objects)
                        Response(gson.toJson(invoke), false)
                    }
                    else -> Response(null, false)
                }
            }
        }
    }

    class IPCService0 : IPCService()
    class IPCService1 : IPCService()
    class IPCService2 : IPCService()

    /**
     * 反序列化参数
     */
    fun restoreParameters(parameters: Array<Parameters?>?): Array<Any?> {
        val objects = arrayOfNulls<Any>(parameters?.size ?: 0)

        for (i in parameters!!.indices) {
            val parameter = parameters[i]
            try {
                objects[i] = gson.fromJson<Any>(parameter?.value, Class.forName(parameter?.type!!))
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return objects
    }
}
