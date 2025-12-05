package com.lotusreichhart.gencanvas.feature.editing.presentation.components

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.RectEvaluator
import android.animation.ValueAnimator
import android.graphics.Rect
import android.net.Uri
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.canhub.cropper.CropImageView
import com.lotusreichhart.gencanvas.feature.editing.domain.model.CropStyle
import java.io.File
import kotlin.math.roundToInt

/**
 * Component hiển thị khung cắt ảnh.
 */
@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@Composable
internal fun CropperTool(
    modifier: Modifier = Modifier,
    imageUri: Uri,
    cropStyle: CropStyle?,
    shouldExecuteCrop: Boolean,
    onCropSuccess: (Uri) -> Unit,
    onCropError: (Exception?) -> Unit
) {
    val context = LocalContext.current
    var cropImageView: CropImageView? by remember { mutableStateOf(null) }

    var currentAnimator: ValueAnimator? by remember { mutableStateOf(null) }

    LaunchedEffect(shouldExecuteCrop) {
        if (shouldExecuteCrop) {
            cropImageView?.croppedImageAsync()
        }
    }

    LaunchedEffect(cropStyle) {
        val view = cropImageView ?: return@LaunchedEffect
        val wholeImageRect = view.wholeImageRect ?: return@LaunchedEffect
        val currentRect = view.cropRect ?: return@LaunchedEffect

        currentAnimator?.cancel()

        val targetRect: Rect? = when {
            cropStyle is CropStyle.Free || cropStyle is CropStyle.Original -> wholeImageRect

            cropStyle?.aspectRatio != null -> {
                calculateTargetRect(
                    currentRect,
                    wholeImageRect,
                    cropStyle.aspectRatio.x.toFloat(),
                    cropStyle.aspectRatio.y.toFloat()
                )
            }

            else -> null
        }

        if (targetRect != null) {
            view.setFixedAspectRatio(false)

            val animator = ValueAnimator.ofObject(RectEvaluator(), currentRect, targetRect)
            animator.duration = 300
            animator.interpolator = AccelerateDecelerateInterpolator()

            animator.addUpdateListener { animation ->
                val animatedRect = animation.animatedValue as Rect
                view.cropRect = animatedRect
            }

            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    applyFinalStyleAttributes(view, cropStyle)
                }
            })

            currentAnimator = animator
            animator.start()
        } else {
            applyFinalStyleAttributes(view, cropStyle)
        }
    }

    Box(modifier = modifier) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                CropImageView(ctx).apply {
                    setImageUriAsync(imageUri)
                    guidelines = CropImageView.Guidelines.ON
                    scaleType = CropImageView.ScaleType.FIT_CENTER
                    isAutoZoomEnabled = true
                    isShowProgressBar = false
                    setFixedAspectRatio(false)

                    setOnCropImageCompleteListener { _, result ->
                        if (result.isSuccessful) {
                            val resultUri = result.uriContent
                                ?: result.getUriFilePath(context, true)?.let {
                                    Uri.fromFile(File(it))
                                }

                            if (resultUri != null) {
                                onCropSuccess(resultUri)
                            } else {
                                onCropError(Exception("Crop URI is null"))
                            }
                        } else {
                            onCropError(result.error)
                        }
                    }
                }.also {
                    cropImageView = it
                }
            }
        )
    }
}

private fun applyFinalStyleAttributes(view: CropImageView, style: CropStyle?) {
    view.cropShape = if (style is CropStyle.Circle) {
        CropImageView.CropShape.OVAL
    } else {
        CropImageView.CropShape.RECTANGLE
    }

    when {
        style is CropStyle.Free -> {
            view.setFixedAspectRatio(false)
        }

        style is CropStyle.Original -> {
            view.setFixedAspectRatio(true)
            val w = view.wholeImageRect?.width() ?: 1
            val h = view.wholeImageRect?.height() ?: 1
            view.setAspectRatio(w, h)
        }

        style?.aspectRatio != null -> {
            view.setFixedAspectRatio(true)
            view.setAspectRatio(style.aspectRatio.x, style.aspectRatio.y)
        }

        else -> {}
    }
}

/**
 * Thuật toán tính toán Rect đích dựa trên tỷ lệ mới.
 * Logic: Cố gắng giữ tâm (Center) của khung hiện tại.
 * Thu hẹp chiều rộng hoặc chiều cao để khớp tỷ lệ mà vẫn nằm trong ảnh.
 */
private fun calculateTargetRect(current: Rect, bounds: Rect, ratioX: Float, ratioY: Float): Rect {
    val currentCenterX = current.centerX()
    val currentCenterY = current.centerY()

    val currentRatio = current.width().toFloat() / current.height().toFloat()
    val targetRatio = ratioX / ratioY

    var newWidth: Float
    var newHeight: Float

    if (targetRatio > currentRatio) {
        newHeight = current.height().toFloat()
        newWidth = newHeight * targetRatio
    } else {
        newWidth = current.width().toFloat()
        newHeight = newWidth / targetRatio
    }

    if (newWidth > bounds.width()) {
        newWidth = bounds.width().toFloat()
        newHeight = newWidth / targetRatio
    }

    if (newHeight > bounds.height()) {
        newHeight = bounds.height().toFloat()
        newWidth = newHeight * targetRatio
    }

    val halfW = (newWidth / 2).roundToInt()
    val halfH = (newHeight / 2).roundToInt()

    var left = currentCenterX - halfW
    var top = currentCenterY - halfH
    var right = currentCenterX + halfW
    var bottom = currentCenterY + halfH

    if (left < bounds.left) {
        val diff = bounds.left - left
        left += diff
        right += diff
    }
    if (top < bounds.top) {
        val diff = bounds.top - top
        top += diff
        bottom += diff
    }
    if (right > bounds.right) {
        val diff = right - bounds.right
        left -= diff // Dịch sang trái
        right -= diff // Dịch sang trái
    }
    if (bottom > bounds.bottom) {
        val diff = bottom - bounds.bottom
        top -= diff // Dịch lên trên
        bottom -= diff // Dịch lên trên
    }

    return Rect(left, top, right, bottom)
}