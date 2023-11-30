package com.example.giftgame.common.user

import android.view.MotionEvent
import android.view.View

class DragExperimentTouchListener(minX: Float, maxX: Float, positionUserCallback: PositionUserCallback) : View.OnTouchListener {
    var isDragging = false
    var deltaX = 0f
    var min = minX
    var max = maxX
    var positionPersonCallback = positionUserCallback

    override fun onTouch(arg0: View, arg1: MotionEvent): Boolean {
        val action: Int = arg1.action
        if (action == MotionEvent.ACTION_DOWN && !isDragging) {
            isDragging = true
            deltaX = arg1.x
            return true
        } else if (isDragging) {
            when (action) {
                MotionEvent.ACTION_MOVE -> {
                    arg0.x = if ((arg0.x + arg1.x - deltaX) > min && (arg0.x + arg1.x - deltaX) < max) {
                        arg0.x + arg1.x - deltaX
                    } else if ((arg0.x + arg1.x - deltaX) < min) {
                        min
                    } else if ((arg0.x + arg1.x - deltaX) > max) {
                        max
                    } else {
                        min
                    }
                    arg0.y = arg0.y
                    positionPersonCallback.onChangePosition(arg0.x.toInt(), arg0.y.toInt())
                    return true
                }

                MotionEvent.ACTION_UP -> {
                    isDragging = false
                    return true
                }

                MotionEvent.ACTION_CANCEL -> {
                    isDragging = false
                    return true
                }
            }
        }
        return false
    }
}