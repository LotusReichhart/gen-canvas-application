package com.lotusreichhart.gencanvas

import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.lotusreichhart.core.ui.theme.GenCanvasTheme
import com.lotusreichhart.core.ui.event.GlobalUiEventManager
import com.lotusreichhart.core.ui.event.UiEvent
import com.lotusreichhart.gencanvas.navigation.MainNavHost
import com.lotusreichhart.gencanvas.presentation.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import com.lotusreichhart.core.ui.theme.Error
import com.lotusreichhart.core.ui.theme.Info
import com.lotusreichhart.core.ui.theme.OnError
import com.lotusreichhart.core.ui.theme.OnInfo
import com.lotusreichhart.core.ui.theme.OnSuccess
import com.lotusreichhart.core.ui.theme.Success

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

                LaunchedEffect(key1 = true) {

                    Log.d("MainActivity LOG", "LaunchedEffect: BẮT ĐẦU lắng nghe UiEvent...")

                    globalUiEventManager.events.collect { event ->

                        Log.d("MainActivity LOG", "UiEvent: ĐÃ NHẬN ĐƯỢC 1 SỰ KIỆN: $event")

                        when (event) {
                            is UiEvent.ShowSnackBar -> {
                                Log.d(
                                    "MainActivity LOG",
                                    "UiEvent là ShowSnackBar, bắt đầu hiển thị...."
                                )
                                snackBarContent = event
                                isSnackBarVisible = true
                                delay(3000L)
                                isSnackBarVisible = false
                            }

                            is UiEvent.ShowToast -> {
                                Log.d(
                                    "MainActivity LOG",
                                    "UiEvent là ShowToast, bắt đầu hiển thị...."
                                )
                            }

                            is UiEvent.ShowDialog -> {
                                Log.d(
                                    "MainActivity LOG",
                                    "UiEvent là ShowDialog, bắt đầu hiển thị...."
                                )
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
                            val navController = rememberNavController()

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