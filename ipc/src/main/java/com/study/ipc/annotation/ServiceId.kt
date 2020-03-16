package com.study.ipc.annotation

/**
 * Date:2020/3/15
 * author:lwb
 * Desc:
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME) // 保留 ，反射用
annotation class ServiceId(val value: String) {
}
