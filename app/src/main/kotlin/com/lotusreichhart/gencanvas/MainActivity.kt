package com.lotusreichhart.gencanvas

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.lotusreichhart.gencanvas.core.common.event.GlobalUiEventManager
import com.lotusreichhart.gencanvas.core.common.event.UiEvent
import com.lotusreichhart.gencanvas.core.ui.theme.GenCanvasTheme
import com.lotusreichhart.gencanvas.navigation.MainNavHost
import com.lotusreichhart.gencanvas.presentation.MainViewModel
import com.lotusreichhart.gencanvas.presentation.components.GenCanvasDialog
import com.lotusreichhart.gencanvas.presentation.components.GenCanvasSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import timber.log.Timber
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var globalUiEventManager: GlobalUiEventManager

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            viewModel.uiState.value.isLoading
        }

        enableEdgeToEdge()
        setContent {
            GenCanvasTheme(
                dynamicColor = false
            ) {

                var snackBarContent by remember { mutableStateOf<UiEvent.ShowSnackBar?>(null) }
                var isSnackBarVisible by remember { mutableStateOf(false) }

                var dialogEvent by remember { mutableStateOf<UiEvent.ShowDialog?>(null) }

                val navController = rememberNavController()

                LaunchedEffect(key1 = true) {
                    Timber.d("LaunchedEffect: BẮT ĐẦU lắng nghe UiEvent...")

                    globalUiEventManager.events.collect { event ->
                        Timber.d("UiEvent: ĐÃ NHẬN ĐƯỢC 1 SỰ KIỆN: $event")

                        when (event) {
                            is UiEvent.ShowSnackBar -> {
                                Timber.d("UiEvent là ShowSnackBar, bắt đầu hiển thị....")
                                snackBarContent = event
                                isSnackBarVisible = true
                                delay(2000L)
                                isSnackBarVisible = false
                            }

                            is UiEvent.ShowToast -> {
                                Timber.d("UiEvent là ShowToast, bắt đầu hiển thị....")
                            }

                            is UiEvent.ShowDialog -> {
                                Timber.d("UiEvent là ShowDialog, bắt đầu hiển thị....")
                                dialogEvent = event
                            }

                            is UiEvent.Navigate -> {
                                Timber.d("UiEvent là Navigate, bắt đầu điều hướng đến -> ${event.route}")
                                navController.navigate(event.route)
                            }
                        }
                    }
                }

                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (!uiState.isLoading) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            MainNavHost(
                                navController = navController,
                                startDestination = uiState.startDestination
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
                        GenCanvasSnackBar(
                            message = snackBarContent?.message?.asString() ?: "",
                            type = snackBarContent?.type ?: UiEvent.SnackBarType.INFO,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                        )
                    }

                    if (dialogEvent != null) {
                        GenCanvasDialog(
                            title = dialogEvent!!.title.asString(),
                            message = dialogEvent!!.message.asString(),

                            positiveButtonText = dialogEvent!!.positiveButtonText.asString(),
                            onPositiveClick = {
                                dialogEvent!!.onPositiveClick()
                            },

                            negativeButtonText = dialogEvent!!.negativeButtonText?.asString(),
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