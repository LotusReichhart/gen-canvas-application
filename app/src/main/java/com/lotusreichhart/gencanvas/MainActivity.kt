package com.lotusreichhart.gencanvas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.lotusreichhart.core.ui.components.AppTextButton
import com.lotusreichhart.core.ui.constant.Dimension
import com.lotusreichhart.core.ui.event.GlobalUiEventManager
import com.lotusreichhart.core.ui.event.UiEvent
import com.lotusreichhart.core.ui.theme.Error
import com.lotusreichhart.core.ui.theme.GenCanvasTheme
import com.lotusreichhart.core.ui.theme.Info
import com.lotusreichhart.core.ui.theme.OnError
import com.lotusreichhart.core.ui.theme.OnInfo
import com.lotusreichhart.core.ui.theme.OnSuccess
import com.lotusreichhart.core.ui.theme.Success
import com.lotusreichhart.core.utils.logD
import com.lotusreichhart.gencanvas.navigation.MainNavHost
import com.lotusreichhart.gencanvas.presentation.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var globalUiEventManager: GlobalUiEventManager

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            viewModel.isLoading.value
        }

        enableEdgeToEdge() // bật tràn viền
        setContent {
            GenCanvasTheme(
                dynamicColor = false
            ) {

                var snackBarContent by remember { mutableStateOf<UiEvent.ShowSnackBar?>(null) }
                var isSnackBarVisible by remember { mutableStateOf(false) }

                var dialogEvent by remember { mutableStateOf<UiEvent.ShowDialog?>(null) }

                val navController = rememberNavController()

                LaunchedEffect(key1 = true) {

                    logD("LaunchedEffect: BẮT ĐẦU lắng nghe UiEvent...")

                    globalUiEventManager.events.collect { event ->

                        logD("UiEvent: ĐÃ NHẬN ĐƯỢC 1 SỰ KIỆN: $event")

                        when (event) {
                            is UiEvent.ShowSnackBar -> {
                                logD(
                                    "MainActivity LOG",
                                    "UiEvent là ShowSnackBar, bắt đầu hiển thị...."
                                )
                                snackBarContent = event
                                isSnackBarVisible = true
                                delay(3000L)
                                isSnackBarVisible = false
                            }

                            is UiEvent.ShowToast -> {
                                logD(
                                    "MainActivity LOG",
                                    "UiEvent là ShowToast, bắt đầu hiển thị...."
                                )
                            }

                            is UiEvent.ShowDialog -> {
                                logD(
                                    "MainActivity LOG",
                                    "UiEvent là ShowDialog, bắt đầu hiển thị...."
                                )
                                dialogEvent = event
                            }

                            is UiEvent.Navigate -> {
                                logD("MainActivity LOG", "Nhận lệnh điều hướng đến: ${event.route}")
                                navController.navigate(event.route)
                            }
                        }
                    }
                }

                val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
                val startDestination by viewModel.startDestination.collectAsStateWithLifecycle()

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (!isLoading) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            MainNavHost(
                                navController = navController,
                                startDestination = startDestination
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = isSnackBarVisible,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .statusBarsPadding(),
                        enter = slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(500)
                        ) + fadeIn(animationSpec = tween(500)),
                        exit = slideOutVertically(
                            targetOffsetY = { it },
                            animationSpec = tween(500)
                        ) + fadeOut(animationSpec = tween(500))
                    ) {
                        AppSnackBar(
                            message = snackBarContent?.message ?: "",
                            type = snackBarContent?.type ?: UiEvent.SnackBarType.INFO,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                        )
                    }

                    if (dialogEvent != null) {
                        AppDialog(
                            title = dialogEvent!!.title,
                            message = dialogEvent!!.message,

                            positiveButtonText = dialogEvent!!.positiveButtonText,
                            onPositiveClick = {
                                dialogEvent!!.onPositiveClick()
                            },

                            negativeButtonText = dialogEvent!!.negativeButtonText,
                            onNegativeClick = {
                                dialogEvent!!.onNegativeClick?.invoke()
                            },

                            onDismiss = {
                                dialogEvent = null
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AppSnackBar(
    message: String,
    type: UiEvent.SnackBarType,
    modifier: Modifier = Modifier
) {

    val backgroundColor = when (type) {
        UiEvent.SnackBarType.INFO -> Info
        UiEvent.SnackBarType.SUCCESS -> Success
        UiEvent.SnackBarType.ERROR -> Error
    }

    val textColor = when (type) {
        UiEvent.SnackBarType.INFO -> OnInfo
        UiEvent.SnackBarType.SUCCESS -> OnSuccess
        UiEvent.SnackBarType.ERROR -> OnError
    }

    val icon = when (type) {
        UiEvent.SnackBarType.INFO -> Icons.Default.Info
        UiEvent.SnackBarType.SUCCESS -> Icons.Default.CheckCircle
        UiEvent.SnackBarType.ERROR -> Icons.Default.Warning
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor,
            modifier = Modifier.weight(1f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppDialog(
    title: String,
    message: String,
    positiveButtonText: String,
    onPositiveClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    negativeButtonText: String? = null,
    onNegativeClick: (() -> Unit)? = null
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dimension.CornerRadius))
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(Dimension.PaddingMedium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(bottom = Dimension.PaddingNormal),
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier.padding(bottom = Dimension.PaddingMedium),
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = Dimension.LargeTitleFontSize
            )

            if (negativeButtonText != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AppTextButton(
                        text = negativeButtonText,
                        textColor = MaterialTheme.colorScheme.onSurface,
                        onClick = {
                            onNegativeClick?.invoke()
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f)
                    )

                    AppTextButton(
                        text = positiveButtonText,
                        textColor = MaterialTheme.colorScheme.secondary,
                        onClick = {
                            onPositiveClick()
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                AppTextButton(
                    text = positiveButtonText,
                    onClick = {
                        onPositiveClick()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}