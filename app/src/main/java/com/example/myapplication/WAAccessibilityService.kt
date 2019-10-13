package com.example.myapplication

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.view.MotionEventCompat.getSource
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T

class WAAccessibilityService : AccessibilityService() {
    private val TAG = "WA-Accessibility"
    private val HIERARCHY = "Hierarchy"
    override fun onInterrupt() {}

    public override fun onServiceConnected() {

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        var event = event ?: return

        Log.d(TAG, "Node: " + event.getEventType())
        Log.d(TAG, "TYPE_NOTIFICATION_STATE_CHANGED: " + AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED)
        Log.d(TAG, "TYPE_WINDOW_CONTENT_CHANGED: " + AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED)

        handleNotificationStateChangedOnEvent(event)
        handleWindowChangedOnEvent(event)
    }

    fun printHierarchyTreeFor(nodeInfo: AccessibilityNodeInfo) {
        var childsCount = nodeInfo.childCount
        var index  = 0

        if(childsCount == 0) {
            return
        }

        while (index < childsCount) {
            var child = nodeInfo.getChild(index)
            Log.d(HIERARCHY, "Node: " + child.viewIdResourceName)
            printHierarchyTreeFor(child)
            index++
        }
    }

    fun handleWindowChangedOnEvent(event: AccessibilityEvent) {
        //  printHierarchyTreeFor(rootInActiveWindow)
        if (event.eventType === AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            var source = event.source

            if (source.getPackageName().equals("com.whatsapp")) {
                val currentNode = rootInActiveWindow

                currentNode.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send").first().performAction(AccessibilityNodeInfo.ACTION_CLICK)

                //currentNode.findAccessibilityNodeInfosByViewId("com.whatsapp:id/back").first().performAction(AccessibilityNodeInfo.ACTION_CLICK)
                //currentNode.findAccessibilityNodeInfosByViewId("com.whatsapp:id/search").first().performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        }
    }

    fun handleNotificationStateChangedOnEvent(event: AccessibilityEvent) {
        var name = ""

        if (event.getEventType() === AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {

            if (event.getPackageName().toString().equals("com.whatsapp")) {

                val message = StringBuilder()
                if (!event.getText().isEmpty()) {
                    for (subText in event.getText()) {
                        message.append(subText)
                    }
                    if (message.toString().contains("Message from")) {
                        name = message.toString().substring(13)
                    }
                }
            }
        }

        Log.d(TAG, name)
    }

}
