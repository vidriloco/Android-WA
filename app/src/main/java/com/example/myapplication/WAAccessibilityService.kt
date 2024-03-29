package com.example.myapplication

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.util.Log
import android.accessibilityservice.AccessibilityServiceInfo

class WAAccessibilityService : AccessibilityService() {
    private val TAG = "WA-Accessibility"
    private val HIERARCHY = "Hierarchy"
    override fun onInterrupt() {}

    public override fun onServiceConnected() {
        val info = AccessibilityServiceInfo()
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK
        info.notificationTimeout = 100
        info.packageNames = null
        serviceInfo = info
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
                val node = currentNode.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send").firstOrNull()

                if(node!= null) {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                }

                //currentNode.findAccessibilityNodeInfosByViewId("com.whatsapp:id/back").first().performAction(AccessibilityNodeInfo.ACTION_CLICK)
                //currentNode.findAccessibilityNodeInfosByViewId("com.whatsapp:id/search").first().performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        }
    }

    fun handleNotificationStateChangedOnEvent(event: AccessibilityEvent) {
        var name = ""

        if (event.eventType === AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {

            if (event.getPackageName().toString().equals("com.whatsapp")) {
                Log.d(TAG, event.getText().toString())

                val message = StringBuilder()
                if (!event.getText().isEmpty()) {

                    for (subText in event.getText()) {
                        message.append(subText)
                    }

                    // Change the text here to "Mensaje de" depending on the language settings
                    // to retrieve the message body on the notification
                    if (message.toString().contains("Message from")) {
                        name = message.toString().substring(13)
                    }
                }
            }
        }

        Log.d(TAG, name)
    }

}
