package com.study.ipc

import android.content.Context

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
            vararg parameters: Any
        ): T? {
            /**
             * 服务：Location,
             * 方法名：getInstance
             * 参数：[]
             */
            return getInstanceWithName(service, instanceClass, "getInstance", parameters)
        }

        private fun <T> getInstanceWithName(
            service: Class<out IPCService>,
            instanceClass: Class<T>,
            methodName: String,
            parameters: Array<out Any>
        ): T? {
            if (!instanceClass.isInterface){
                throw IllegalArgumentException("must use interface")
            }
            //TODO 委婉待续
            return null
        }
    }

}