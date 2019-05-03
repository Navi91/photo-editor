package com.dkrasnov.photoeditor.editor

import android.util.Log
import android.view.MotionEvent
import android.view.View

class StickerTouchListener(private val view: View) : View.OnTouchListener {

    companion object {
        private const val TAG = "StickerTouchListener"

        private const val INVALID_POINTER_ID = -1
    }

    private var activePointerId = INVALID_POINTER_ID
    private var previousX = 0f
    private var previousY = 0f
    private var mPosX = 0f
    private var mPosY = 0f
    private val scaleDetector = MyScaleGestureDetector(StickerScaleGestureListener())

    inner class StickerScaleGestureListener : MyScaleGestureDetector.OnScaleGestureListener {

        private var pivotX = 0f
        private var pivotY = 0f
        private var previousSpanPoint = Vector2D()

        override fun onScaleBegin(view: View?, detector: MyScaleGestureDetector): Boolean {
            Log.d(TAG, "onScaleBegin")

            pivotX = detector.focusX
            pivotY = detector.focusY

            previousSpanPoint.set(detector.currentSpanVector)

            return true
        }

        override fun onScale(view: View, detector: MyScaleGestureDetector): Boolean {
            Log.d(TAG, "onScale")

//            scale(detector.scaleFactor)

            val deltaAngle = Vector2D.getAngle(previousSpanPoint, detector.currentSpanVector)

//            rotate(deltaAngle)

            val info = TransformInfo()
            info.deltaScale = detector.scaleFactor
            info.deltaAngle = Vector2D.getAngle(previousSpanPoint, detector.currentSpanVector)
            info.deltaX = detector.focusX - pivotX
            info.deltaY = detector.focusY - pivotY
            info.pivotX = pivotX
            info.pivotY = pivotY
            info.minimumScale = 0.1f
            info.maximumScale = 2f
            move(view, info)

            return false
        }

        override fun onScaleEnd(view: View?, detector: MyScaleGestureDetector?) {

        }
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {

        scaleDetector.onTouchEvent(view, event)

        when (event.action and event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                event.actionIndex.also { pointerIndex ->
                    // Remember where we started (for dragging)
                    previousX = event.getX(pointerIndex)
                    previousY = event.getY(pointerIndex)

                    activePointerId = event.getPointerId(pointerIndex)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                // Find the index of the active pointer and fetch its position
                event.findPointerIndex(activePointerId).takeIf { it != INVALID_POINTER_ID }.let {
                    val (x: Float, y: Float) = event.actionIndex.let { pointerIndex ->
                        // Calculate the distance moved
                        event.getX(pointerIndex) to event.getY(pointerIndex)
                    }

                    if (!scaleDetector.isInProgress) {
                        translate(view, x - previousX, y - previousY)
                    }
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                activePointerId = INVALID_POINTER_ID
            }
            MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndexPointerUp =
                    event.action and MotionEvent.ACTION_POINTER_INDEX_MASK shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
                val pointerId = event.getPointerId(pointerIndexPointerUp)

                if (pointerId == activePointerId) {
                    val newPointerIndex = if (pointerIndexPointerUp == 0) 1 else 0
                    previousX = event.getX(newPointerIndex)
                    previousY = event.getY(newPointerIndex)
                    activePointerId = event.getPointerId(newPointerIndex)
                }

//                event.actionIndex.also { pointerIndex ->
//                    event.getPointerId(pointerIndex)
//                        .takeIf { it == activePointerId }
//                        ?.run {
//                            // This was our active pointer going up. Choose a new
//                            // active pointer and adjust accordingly.
//                            val newPointerIndex = if (pointerIndex == 0) 1 else 0
//                            previousX = event.getX(newPointerIndex)
//                            previousY = event.getY(newPointerIndex)
//                            activePointerId = event.getPointerId(newPointerIndex)
//                        }
//                }
            }
        }
        return true
    }

    private fun move(view: View, info: TransformInfo) {
//        computeRenderOffset(view, info.pivotX, info.pivotY)
//        translate(view, info.deltaX, info.deltaY)

        scale(info.deltaScale)
        rotate(info.deltaAngle)
    }

    private fun scale(scaleFactor: Float) {
        var scale = view.scaleX * scaleFactor
        scale = Math.max(0.1f, Math.min(scale, 2f))

        view.scaleX = scale
        view.scaleY = scale
    }

    private fun rotate(deltaAngle: Float) {
        val rotation = (view.rotation + deltaAngle).adjustDegree()
        view.rotation = rotation
    }

    private fun Float.adjustDegree(): Float {

        return when {
            this > 180f -> this - 360f
            this < 180f -> this + 360f
            else -> this
        }
    }

    private fun translate(view: View, deltaX: Float, deltaY: Float) {
        Log.d(TAG, "translate $deltaX $deltaY")

        val deltaVector = floatArrayOf(deltaX / view.scaleX, deltaY / view.scaleY)
        view.matrix.mapVectors(deltaVector)
        view.run {
            matrix.mapVectors(deltaVector)
            translationX += deltaVector[0]
            translationY += deltaVector[1]
        }
    }

    private fun computeRenderOffset(view: View, pivotX: Float, pivotY: Float) {
        if (view.pivotX == pivotX && view.pivotY == pivotY) {
            return
        }

        val prevPoint = floatArrayOf(0.0f, 0.0f)
        view.matrix.mapPoints(prevPoint)

        view.pivotX = pivotX
        view.pivotY = pivotY

        val currPoint = floatArrayOf(0.0f, 0.0f)
        view.matrix.mapPoints(currPoint)

        val offsetX = currPoint[0] - prevPoint[0]
        val offsetY = currPoint[1] - prevPoint[1]

        view.translationX = view.translationX - offsetX
        view.translationY = view.translationY - offsetY
    }

    private inner class TransformInfo {
        internal var deltaX: Float = 0.toFloat()
        internal var deltaY: Float = 0.toFloat()
        internal var deltaScale: Float = 0.toFloat()
        internal var deltaAngle: Float = 0.toFloat()
        internal var pivotX: Float = 0.toFloat()
        internal var pivotY: Float = 0.toFloat()
        internal var minimumScale: Float = 0.toFloat()
        internal var maximumScale: Float = 0.toFloat()
    }
}