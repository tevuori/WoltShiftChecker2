package com.example.woltshiftchecker

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class MyAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Zde bude logika pro detekci změn
    }

    override fun onInterrupt() {
        // Zpracování přerušení
    }
}