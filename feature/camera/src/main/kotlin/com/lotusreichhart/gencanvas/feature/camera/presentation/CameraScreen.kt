package com.lotusreichhart.gencanvas.feature.camera.presentation

import android.annotation.SuppressLint
import android.net.Uri
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalZeroShutterLag
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.FlipCameraIos
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.lotusreichhart.gencanvas.core.ui.components.GenCanvasIconButton
import kotlinx.coroutines.delay
import timber.log.Timber
import java.io.File

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.lotusreichhart.gencanvas.core.ui.components.GenCanvasTextButton
import com.lotusreichhart.gencanvas.core.ui.constant.Dimension
import com.lotusreichhart.gencanvas.feature.camera.domain.model.CameraRatio
import com.smarttoolfactory.slider.ColorfulSlider
import com.smarttoolfactory.slider.MaterialSliderDefaults
import com.smarttoolfactory.slider.SliderBrushColor
import kotlin.math.roundToInt

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalZeroShutterLag::class)
@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@Composable
internal fun CameraScreen(
    onImageCaptured: (Uri) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var selectedRatio by rememberSaveable { mutableStateOf(CameraRatio.RATIO_4_3) }
    var isGridEnabled by rememberSaveable { mutableStateOf(false) }

    var flashMode by rememberSaveable { mutableIntStateOf(ImageCapture.FLASH_MODE_OFF) }
    var showCaptureEffect by remember { mutableStateOf(false) }

    val cameraController = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE or CameraController.VIDEO_CAPTURE)
            imageCaptureMode = ImageCapture.CAPTURE_MODE_ZERO_SHUTTER_LAG
        }
    }

    val zoomState by cameraController.zoomState.observeAsState()
    val exposureState = remember(zoomState) {
        cameraController.cameraInfo?.exposureState
    }

    var sliderValue by remember { mutableFloatStateOf(0f) }
    var lastExposureIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(selectedRatio) {
        cameraController.unbind()
        cameraController.imageCaptureResolutionSelector = ResolutionSelector.Builder()
            .setAllowedResolutionMode(ResolutionSelector.PREFER_HIGHER_RESOLUTION_OVER_CAPTURE_RATE)
            .setAspectRatioStrategy(
                AspectRatioStrategy(
                    selectedRatio.aspectRatioStrategy,
                    AspectRatioStrategy.FALLBACK_RULE_AUTO
                )
            )
            .build()
        cameraController.bindToLifecycle(lifecycleOwner)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .aspectRatio(selectedRatio.floatRatio)
                .fillMaxWidth()
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                        controller = cameraController
                    }
                },
                onRelease = { cameraController.unbind() }
            )

            if (isGridEnabled) {
                GridOverlay(modifier = Modifier.fillMaxSize())
            }

            if (exposureState?.isExposureCompensationSupported == true) {
                val range = exposureState.exposureCompensationRange
                val step = exposureState.exposureCompensationStep
                val valueRange = range.lower.toFloat()..range.upper.toFloat()

                val evValue = sliderValue * step.toFloat()
                val displayValue =
                    if (evValue > 0) "+%.1f".format(evValue) else "%.1f".format(evValue)

                CenterBar(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    title = displayValue,
                    value = sliderValue,
                    valueRange = valueRange,
                    onChange = {
                        sliderValue = it
                        val newIndex = it.roundToInt()
                        if (newIndex != lastExposureIndex) {
                            lastExposureIndex = newIndex
                            cameraController.cameraControl?.setExposureCompensationIndex(
                                newIndex
                            )
                        }
                    },
                    onClick = {
                        sliderValue = 0f
                        cameraController.cameraControl?.setExposureCompensationIndex(0)
                    }
                )
            }
        }


        if (showCaptureEffect) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            )
            LaunchedEffect(Unit) {
                delay(50)
                showCaptureEffect = false
            }
        }

        TopBar(
            label = selectedRatio.label,
            flashMode = flashMode,
            isGridEnabled = isGridEnabled,
            onRatioClick = { selectedRatio = selectedRatio.next() },
            onGridClick = { isGridEnabled = it },
            onFlashClick = {
                flashMode =
                    if (flashMode == ImageCapture.FLASH_MODE_OFF) ImageCapture.FLASH_MODE_ON
                    else ImageCapture.FLASH_MODE_OFF
                cameraController.imageCaptureFlashMode = flashMode

            },
            onClose = onClose
        )

        BottomBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            cameraController = cameraController,
            onTake = {
                val file = File.createTempFile("cam_", ".jpg", context.cacheDir)
                val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

                cameraController.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            showCaptureEffect = true
                            onImageCaptured(Uri.fromFile(file))
                        }

                        override fun onError(exc: ImageCaptureException) {
                            Timber.e("Lỗi: ${exc.message}")
                        }
                    }
                )
            },
            onCamera = {
                val newSelector =
                    if (cameraController.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                        CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA
                cameraController.cameraSelector = newSelector
            }
        )
    }
}

@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    label: String,
    flashMode: Int,
    isGridEnabled: Boolean,
    onRatioClick: () -> Unit,
    onGridClick: (Boolean) -> Unit,
    onFlashClick: () -> Unit,
    onClose: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(Dimension.Spacing.l)
    ) {
        GenCanvasIconButton(
            modifier = Modifier.align(Alignment.CenterStart),
            imageVector = Icons.Default.Close,
            onClick = onClose,
        )

        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .background(Color.Black.copy(0.3f), CircleShape)
                .padding(horizontal = Dimension.Spacing.s),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.m)
        ) {
            GenCanvasTextButton(
                text = label,
                textColor = Color.White,
                textStyle = TextStyle(
                    fontWeight = FontWeight.Bold
                ),
                onClick = onRatioClick
            )

            GenCanvasIconButton(
                modifier = Modifier
                    .padding(Dimension.Spacing.s)
                    .size(Dimension.Icon.m),
                imageVector = Icons.Default.GridOn,
                tint = if (isGridEnabled) Color.Yellow else Color.White,
                onClick = {
                    onGridClick(!isGridEnabled)
                }
            )
        }

        GenCanvasIconButton(
            modifier = Modifier.align(Alignment.CenterEnd),
            imageVector = if (flashMode == ImageCapture.FLASH_MODE_ON) Icons.Default.FlashOn else Icons.Default.FlashOff,
            tint = if (flashMode == ImageCapture.FLASH_MODE_ON) Color.Yellow else Color.White,
            onClick = onFlashClick
        )
    }
}

@Composable
private fun CenterBar(
    modifier: Modifier = Modifier,
    title: String,
    value: Float = 0F,
    valueRange: ClosedFloatingPointRange<Float>,
    onChange: (Float) -> Unit,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(end = Dimension.Spacing.m)
            .width(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.Black.copy(0.5f), CircleShape)
                .border(1.dp, Color.Yellow, CircleShape)
                .clip(CircleShape)
                .clickable {
                    onClick()
                },
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = title,
                color = Color.Yellow,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(220.dp)
                .width(40.dp)
        ) {
            ColorfulSlider(
                value = value,
                onValueChange = { newVal: Float ->
                    onChange(newVal)
                },
                valueRange = valueRange,
                trackHeight = 4.dp,
                thumbRadius = 10.dp,
                colors = MaterialSliderDefaults.materialColors(
                    activeTrackColor = SliderBrushColor(Color.Yellow),
                    inactiveTrackColor = SliderBrushColor(Color.White.copy(0.5f)),
                    thumbColor = SliderBrushColor(Color.Yellow)
                ),
                modifier = Modifier
                    .graphicsLayer {
                        rotationZ = 270f
                        transformOrigin = TransformOrigin(0.5f, 0.5f)
                    }
                    .requiredWidth(200.dp)
                    .requiredHeight(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Icon(
            imageVector = Icons.Default.WbSunny,
            contentDescription = null,
            tint = Color.White.copy(0.7f),
            modifier = Modifier.size(20.dp)
        )
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun BottomBar(
    modifier: Modifier = Modifier,
    cameraController: LifecycleCameraController,
    onTake: () -> Unit,
    onCamera: () -> Unit
) {
    val zoomState by cameraController.zoomState.observeAsState()

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        zoomState?.let { state ->
            Text(
                text = String.format("%.1fx", state.zoomRatio),
                color = Color.Yellow,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(Color.Black.copy(0.5f), CircleShape)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color.Black.copy(0.7f)
                        )
                    )
                )
                .padding(bottom = 50.dp, top = 20.dp)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.size(48.dp))

            Box(
                modifier = Modifier
                    .size(74.dp)
                    .border(4.dp, Color.White, CircleShape)
                    .clip(CircleShape)
                    .clickable {
                        onTake()
                    }
                    .background(Color.White.copy(alpha = 0.2f))
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .align(Alignment.Center)
                        .background(Color.White, CircleShape)
                )
            }

            GenCanvasIconButton(
                modifier = Modifier.size(48.dp),
                imageVector = Icons.Default.FlipCameraIos,
                tint = Color.White,
                onClick = onCamera,
            )
        }
    }
}

@Composable
private fun GridOverlay(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val color = Color.White.copy(alpha = 0.5f)
        val stroke = 1.dp.toPx()

        val thirdW = w / 3
        val thirdH = h / 3

        drawLine(color, Offset(thirdW, 0f), Offset(thirdW, h), stroke)
        drawLine(color, Offset(thirdW * 2, 0f), Offset(thirdW * 2, h), stroke)

        drawLine(color, Offset(0f, thirdH), Offset(w, thirdH), stroke)
        drawLine(color, Offset(0f, thirdH * 2), Offset(w, thirdH * 2), stroke)

        val centerX = w / 2
        val centerY = h / 2
        val crossLength = 12.dp.toPx()
        val crossGap = 4.dp.toPx()
        val targetColor = Color.Yellow.copy(alpha = 0.8f)
        val targetStroke = 2.dp.toPx()

        drawPath(
            path = Path().apply {
                moveTo(centerX - crossLength, centerY - crossGap)
                lineTo(centerX - crossLength, centerY - crossLength)
                lineTo(centerX - crossGap, centerY - crossLength)
            },
            color = targetColor,
            style = Stroke(width = targetStroke, cap = StrokeCap.Round)
        )
        drawPath(
            path = Path().apply {
                moveTo(centerX + crossGap, centerY - crossLength)
                lineTo(centerX + crossLength, centerY - crossLength)
                lineTo(centerX + crossLength, centerY - crossGap)
            },
            color = targetColor,
            style = Stroke(width = targetStroke, cap = StrokeCap.Round)
        )

        drawPath(
            path = Path().apply {
                moveTo(centerX + crossLength, centerY + crossGap)
                lineTo(centerX + crossLength, centerY + crossLength)
                lineTo(centerX + crossGap, centerY + crossLength)
            },
            color = targetColor,
            style = Stroke(width = targetStroke, cap = StrokeCap.Round)
        )

        drawPath(
            path = Path().apply {
                moveTo(centerX - crossGap, centerY + crossLength)
                lineTo(centerX - crossLength, centerY + crossLength)
                lineTo(centerX - crossLength, centerY + crossGap)
            },
            color = targetColor,
            style = Stroke(width = targetStroke, cap = StrokeCap.Round)
        )

        drawCircle(
            color = targetColor,
            radius = 2.dp.toPx(),
            center = Offset(centerX, centerY)
        )
    }
}