package com.xuran.taro_h5_jiuxiandao

import android.app.Activity
import android.util.Log
import android.webkit.ValueCallback
import android.webkit.WebView
import android.widget.Toast
import java.lang.ref.WeakReference


internal class AndroidInterface private constructor() {
    // 使用弱引用持有相关对象
    private var activityRef = WeakReference<Activity>(null)
    private var webViewRef = WeakReference<WebView>(null)

    companion object {
        @Volatile
        private var instance: AndroidInterface? = null

        fun getInstance(): AndroidInterface {
            return instance ?: synchronized(this) {
                instance ?: AndroidInterface().also { instance = it }
            }
        }
    }

    // 初始化方法（需在Activity中调用）
    fun init(activity: Activity, webView: WebView) {
        activityRef = WeakReference(activity)
        webViewRef = WeakReference(webView)
    }

    // 调用JS 清除 Store
    fun clearStore() {
        val activity = activityRef.get()
        val webView = webViewRef.get()

        activity?.runOnUiThread {
            if (webView != null && activity != null) {
                webView.evaluateJavascript("javascript:H5Api.clearStore()",
                    ValueCallback<String> { value ->
                        if (value == "true") {
                            Toast.makeText(
                                activity ?: getApplicationContext(),
                                "清除数据成功！",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                activity ?: getApplicationContext(),
                                "清除数据失败！",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    })
            } else {
                Toast.makeText(
                    activity ?: getApplicationContext(), "WebView不可用！", Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    // 用于获取Application Context
    private fun getApplicationContext() =
        activityRef.get()?.applicationContext ?: android.app.Application()
}

// 扩展函数简化调用
fun Activity.registerAndroidInterface(webView: WebView) {
    AndroidInterface.getInstance().init(this, webView)
}