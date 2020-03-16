package com.study.ipc

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
    }

    /**
     * 绑定
     */
    fun bind() {

    }
}