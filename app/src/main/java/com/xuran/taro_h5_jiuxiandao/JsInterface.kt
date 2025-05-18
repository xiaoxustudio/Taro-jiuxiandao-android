package com.xuran.taro_h5_jiuxiandao

import android.app.Activity
import android.content.Intent
import android.webkit.JavascriptInterface
import android.webkit.WebStorage
import android.webkit.WebView
import android.widget.Toast
import java.io.File
import java.lang.ref.WeakReference


internal class JsInterface private constructor() {
    // 使用弱引用持有相关对象
    private var activityRef = WeakReference<Activity>(null)
    private var webViewRef = WeakReference<WebView>(null)

    companion object {
        @Volatile
        private var instance: JsInterface? = null

        fun getInstance(): JsInterface {
            return instance ?: synchronized(this) {
                instance ?: JsInterface().also { instance = it }
            }
        }
    }

    // 初始化方法（需在Activity中调用）
    fun init(activity: Activity, webView: WebView) {
        activityRef = WeakReference(activity)
        webViewRef = WeakReference(webView)
    }

    // 清理缓存方法（可跨界面调用）
    @JavascriptInterface
    fun clearStore() {
        val activity = activityRef.get()
        val webView = webViewRef.get()

        activity?.runOnUiThread {
            if (webView != null && activity != null) {
                webView.clearCache(true)
                webView.clearHistory()
                webView.clearMatches()
                webView.clearFormData()
                webView.clearSslPreferences()
                Toast.makeText(activity, "清除数据成功！", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    activity ?: getApplicationContext(), "WebView不可用！", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // 其他方法保持类似结构
    @JavascriptInterface
    fun GoToSetting() {
        activityRef.get()?.let { activity ->
            activity.runOnUiThread {
                val intent = Intent(activity, Setting::class.java)
                activity.startActivity(intent)
            }
        }
    }

    // 用于获取Application Context
    private fun getApplicationContext() =
        activityRef.get()?.applicationContext ?: android.app.Application()
}

// 扩展函数简化调用
fun Activity.registerJsInterface(webView: WebView, name: String) {
    JsInterface.getInstance().init(this, webView)
    webView.addJavascriptInterface(
        JsInterface.getInstance(), name
    )
}