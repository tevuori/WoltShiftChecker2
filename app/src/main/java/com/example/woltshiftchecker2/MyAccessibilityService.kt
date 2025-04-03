package com.example.woltshiftchecker2

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class MyAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            if (event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
                val rootNode = rootInActiveWindow // Kořenový prvek aktuálního okna
                rootNode?.let { node ->
                    // Hledejte prvky podle ID/textu (přizpůsobte podle Wolt UI)
                    val shiftNode = node.findAccessibilityNodeInfosByViewId("com.wolt.android:id/shift_time")
                    if (shiftNode.isNotEmpty()) {
                        val shiftText = shiftNode[0].text
                        Log.d("WoltShift", "Detekovaná směna: $shiftText")
                        // Zde můžete spustit notifikaci nebo uložit data
                    }
                    node.recycle() // Vždy uvolněte prostředky!
                }
            }
        }
    }

    override fun onInterrupt() {
        Log.d("Accessibility", "Service interrupted")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d("Accessibility", "Service connected!") // Potvrďte připojení
    }
}