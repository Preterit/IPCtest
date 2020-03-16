package com.study.ipc

/**
 * Date:2020/3/15
 * author:lwb
 * Desc:
 */
class Ipc {
    companion object{
        //注册接口 ，暴露给外部使用
        fun register(service :Class<*>){
            val registry = Registry.getInstance()
            registry.register(service)
        }
    }
}