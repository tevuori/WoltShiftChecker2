package com.example.woltshiftchecker2

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.os.Handler
import android.os.Looper
import android.app.NotificationManager
import androidx.core.app.NotificationCompat

class MyAccessibilityService : AccessibilityService() {
    private val woltPackageName = "com.wolt.partner" // Upravte podle skutečného package name Wolt Partner
    private var isChecking = false

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            // Detekujte, zda je Wolt Partner v popředí
            if (it.packageName == woltPackageName && !isChecking) {
                isChecking = true
                startAutomation()
            }
        }
    }
    override fun onInterrupt() {}
    private fun startAutomation() {
        // Krok 1: Klikni na menu (ikona vlevo nahoře)
        clickMenuButton()

        // Krok 2: Po 3s klikni na "Scheduled hours"
        Handler(Looper.getMainLooper()).postDelayed({
            clickScheduledHours()
        }, 3000)
    }

    private fun clickMenuButton() {
        // Najdi menu tlačítko podle ID/obsahu (upravte podle Wolt UI)
        val rootNode = rootInActiveWindow
        rootNode?.findAccessibilityNodeInfosByText("Menu")?.firstOrNull()?.let { menuNode ->
            performClick(menuNode)
        }
    }
    private fun performClick(node: AccessibilityNodeInfo) {
        node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        node.recycle() // Důležité pro uvolnění paměti!
    }
    private fun clickScheduledHours() {
        // Najdi "Scheduled hours" v menu
        val rootNode = rootInActiveWindow
        rootNode?.findAccessibilityNodeInfosByText("Scheduled hours")?.firstOrNull()?.let { node ->
            performClick(node)

            // Krok 3: Po dalším zpoždění klikni na "Frýdek-Místek"
            Handler(Looper.getMainLooper()).postDelayed({
                clickLocation()
            }, 3000)
        }
    }

    private fun clickLocation() {
        val rootNode = rootInActiveWindow
        rootNode?.findAccessibilityNodeInfosByText("Frýdek-Místek")?.firstOrNull()?.let { node ->
            performClick(node)

            // Krok 4: Zkontroluj hodnotu "0 / -"
            Handler(Looper.getMainLooper()).postDelayed({
                checkShiftAvailability()
            }, 2000)
        }
    }

    private fun checkShiftAvailability() {
        val rootNode = rootInActiveWindow
        rootNode?.let { node ->
            // Hledejte podle ID nebo přesného textu
            val shiftNodes = node.findAccessibilityNodeInfosByText("0 / -")
            if (shiftNodes.isNotEmpty()) {
                val text = shiftNodes[0].text.toString()
                Log.d("WoltShift", "Detekovaný text: $text")
                // ... (logika pro kontrolu)
            }
        }
    }

    private fun showNotification(message: String) {
        val notificationManager = getSystemService(NotificationManager::class.java)
        val channelId = "shift_channel"

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Wolt Shift Checker")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        notificationManager?.notify(1, notification)
    }
}