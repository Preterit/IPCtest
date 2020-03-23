package com.study.ipc

import android.content.Context
import com.study.ipc.model.Request
import java.lang.reflect.Proxy

/**
 * Date:2020/3/15
 * author:lwb
 * Desc:
 */
class Ipc {
    companion object {
        //注册接口 ，暴露给外部使用
        fun register(service: Class<*>) {
            val registry = Registry.getInstance()
            registry.register(service)
        }

        /**
         * 绑定服务
         */
        fun connect(context: Context, service: Class<out IPCService?>) {
            connect(context, null, service)
        }

        /**
         * 绑定服务 apk包名 跨应用
         */
        fun connect(context: Context, packageName: String?, service: Class<out IPCService?>) {
            Channel.getInstance().bind(context, packageName, service)
        }

        /**
         * 解绑
         */
        fun disConnect(context: Context, service: Class<out IPCService>) {
            Channel.getInstance().unBind(context, service)
        }


        fun <T> getInstance(
            service: Class<out IPCService>,
            instanceClass: Class<T>,
            parameters: Array<Any>
        ): T? {
            /**
             * 服务：Location,
             * 方法名：getInstance
             * 参数：[]
             */
            return getInstanceWithName(service, instanceClass, "getInstance", parameters)
        }

        fun <T> getInstanceWithName(
            service: Class<out IPCService>,
            instanceClass: Class<T>,
            methodName: String,
            vararg parameters: Any
        ): T? {
            if (!instanceClass.isInterface) {
                throw IllegalArgumentException("must use interface")
            }
            val response = Channel.getInstance()
                .send(Request.GET_INSTANCE, service, instanceClass, methodName, parameters)


            if (response.isSuccess) {
                //返回一个假的代理对象
                return getProxy(instanceClass, service)
            }
            return null
        }

        private fun <T> getProxy(instanceClass: Class<T>, service: Class<out IPCService>): T? {
            val classLoader = instanceClass.classLoader

            return Proxy.newProxyInstance(
                classLoader,
                arrayOf<Class<*>>(instanceClass),
                IPCInvocationHandler(instanceClass, service)
            ) as T
        }
    }

}