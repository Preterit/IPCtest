package com.study.ipc

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.text.TextUtils
import com.google.gson.Gson
import com.study.ipc.annotation.ServiceId
import com.study.ipc.model.Parameters
import com.study.ipc.model.Request
import com.study.ipc.model.Response
import java.util.concurrent.ConcurrentHashMap

/**
 * Date:2020/3/15
 * author:lwb
 * Desc:
 */
class Channel {

    companion object {
        //单例
        private var mInstance: Channel? = null

        @JvmStatic
        fun getInstance(): Channel {
            if (null == mInstance) {
                synchronized(Channel::class.java) {
                    if (null == mInstance) {
                        mInstance = Channel()
                    }
                }
            }
            return mInstance!!
        }

        val gson = Gson()
    }

    /**
     * 已经绑定过的
     */
    private val mBinds = ConcurrentHashMap<Class<out IPCService>, Boolean>()

    /**
     * 是否正在绑定
     */
    private val mBinding = ConcurrentHashMap<Class<out IPCService>, Boolean>()

    /**
     * 已经绑定的服务 对应的ServiceConnect
     */
    private val mServiceConnections =
        ConcurrentHashMap<Class<out IPCService>, IPCServiceConnection>()

    /**
     *  已经绑定服务的 IBinder对象
     */
    private val mBinder = ConcurrentHashMap<Class<out IPCService>, IIPCService>()


    /**
     * 连接其他APK进程服务
     *
     * @param context
     * @param packageName apk包名
     * @param service
     */
    fun bind(context: Context, packageName: String?, service: Class<out IPCService?>) {
        var ipcServiceConnection: IPCServiceConnection? = null
        //判断是否已经绑定过了
        val isBound = mBinds[service]
        if (isBound != null && isBound) {
            return
        }
        //判断是否正在绑定
        val isBinding = mBinding[service]
        if (isBinding != null && isBinding) {
            return
        }

        //正在绑定
        mBinding[service] = true
        ipcServiceConnection = IPCServiceConnection(service)
        mServiceConnections[service] = ipcServiceConnection

        var intent: Intent? = null
        if (TextUtils.isEmpty(packageName)) {
            intent = Intent(context, service)
        } else {
            intent = Intent()
            intent.setClassName(packageName!!, service.name)
        }

        context.bindService(intent, ipcServiceConnection, Context.BIND_AUTO_CREATE)
    }


    fun unBind(context: Context, service: Class<out IPCService>) {
        val bound = mBinds[service]
        if (bound != null && bound) {
            val connection = mServiceConnections[service]
            if (connection != null) {
                context.unbindService(connection)
            }
            mBinds[service] = false
        }

    }

    inner class IPCServiceConnection(private val mService: Class<out IPCService>) :
        ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName?) {
            mBinds.remove(mService)
            mBinding.remove(mService)
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val ipcService = IIPCService.Stub.asInterface(service)
            mBinder[mService] = ipcService
            mBinds[mService] = true
            mBinding.remove(mService)
        }
    }

    fun send(
        type: Int,
        service: Class<out IPCService>,
        classType: Class<Any>,
        methodName: String,
        parameters: Array<Any>
    ): Response {
        //绑定IPCService 返回的Binder对象
        val iipcService = mBinder[service]
        return if (iipcService != null) {
            try {
                val annotation = classType.getAnnotation(ServiceId::class.java)
                val serviceId = annotation?.value
                val request = Request(type, serviceId!!, methodName, makeParameter(parameters))
                iipcService.send(request)
            } catch (e: Exception) {
                e.printStackTrace()
                return Response(null, false)
            }
        } else {
            return Response(null, false)
        }
    }


    private fun makeParameter(parameters: Array<Any>?): Array<Parameters?> {
        val p = arrayOfNulls<Parameters>(parameters?.size ?: 0)
        if (parameters != null) {
            for ((i, item) in parameters.withIndex()) {
                p[i] = Parameters(item.javaClass.name, gson.toJson(item))
            }
        }
        return p
    }

}