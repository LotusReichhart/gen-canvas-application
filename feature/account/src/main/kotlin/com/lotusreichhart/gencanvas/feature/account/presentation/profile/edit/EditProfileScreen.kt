package com.lotusreichhart.gencanvas.feature.account.presentation.profile.edit

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.lotusreichhart.gencanvas.core.ui.constant.Dimension

import kotlinx.coroutines.launch
import androidx.core.net.toUri
import com.lotusreichhart.gencanvas.core.common.util.createTempPictureUri
import com.lotusreichhart.gencanvas.core.common.util.toPhysicalFile
import com.lotusreichhart.gencanvas.core.ui.components.GenCanvasBottomSheet
import com.lotusreichhart.gencanvas.core.ui.components.GenCanvasBottomSheetItem
import com.lotusreichhart.gencanvas.core.ui.components.GenCanvasIconButton
import com.lotusreichhart.gencanvas.core.ui.components.GenCanvasTextButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

import com.lotusreichhart.gencanvas.feature.account.R
import com.lotusreichhart.gencanvas.core.common.R as CoreR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditProfileScreen(
    viewModel: EditProfileViewModel = hiltViewModel(),
    editedImageResult: String?,
    onConsumeEditedImageResult: () -> Unit,
    onNavigateToEditor: (Uri) -> Unit,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()

    val isLoading = uiState.isLoading

    val canSubmit = isOnline && !isLoading && uiState.isChanged && uiState.name.isNotEmpty()

    val interactionSource = remember { MutableInteractionSource() }

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    val onSave = {
        scope.launch {
            val currentUri = uiState.avatarUri

            val fileToSend: File? = if (currentUri != null) {
                withContext(Dispatchers.IO) {
                    currentUri.toPhysicalFile(context)
                }
            } else {
                null
            }

            viewModel.onSaveClick(fileToSend)
        }
    }


    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            onNavigateToEditor(uri)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraUri != null) {
            onNavigateToEditor(tempCameraUri!!)
        }
    }

    val launchCameraProcess = {
        val uri = context.createTempPictureUri()
        tempCameraUri = uri
        cameraLauncher.launch(uri)
    }


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            launchCameraProcess()
        } else {
            viewModel.onCameraPermissionDenied()
        }
    }

    val onPickFromGallery = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            showBottomSheet = false
            galleryLauncher.launch("image/*")
        }
    }

    val onTakePhoto = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            showBottomSheet = false
            val permissionCheck = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            )

            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                launchCameraProcess()
            } else {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    LaunchedEffect(editedImageResult) {
        editedImageResult?.let { uriString ->
            val uri = uriString.toUri()
            viewModel.onAvatarSelected(uri)
            onConsumeEditedImageResult()
        }
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onDismiss()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.profile_edit_title),
                        fontSize = Dimension.TextSize.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    GenCanvasIconButton(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel Icon",
                        iconSize = Dimension.Icon.m,
                        onClick = onDismiss,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    MaterialTheme.colorScheme.surfaceVariant
                ),
                actions = {
                    GenCanvasTextButton(
                        text = stringResource(id = CoreR.string.core_action_save),
                        textColor = if (canSubmit) MaterialTheme.colorScheme.onSurfaceVariant
                        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        textStyle = MaterialTheme.typography.labelMedium.copy(
                            fontSize = Dimension.TextSize.titleSmall
                        ),
                        onClick = { onSave() },
                        enabled = canSubmit
                    )
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = Dimension.Spacing.m),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier
                    .size(120.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = {
                            val currentUri = uiState.avatarUri
                            if (currentUri != null) {
                                onNavigateToEditor(currentUri)
                            } else {
                                showBottomSheet = true
                            }
                        }
                    )
            ) {
                val model = uiState.displayAvatarModel
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(model)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary)
                        .clickable {
                            showBottomSheet = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.name,
                onValueChange = { viewModel.onNameChange(it) },
                shape = RoundedCornerShape(Dimension.Radius.m),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.7f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.7f),
                    errorContainerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    cursorColor = MaterialTheme.colorScheme.onSurface,
                    focusedLabelColor = MaterialTheme.colorScheme.secondary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
                ),
                isError = uiState.nameErrorMessage != null,
                singleLine = true,
                supportingText = if (uiState.nameErrorMessage != null) {
                    {
                        Text(
                            text = uiState.nameErrorMessage ?: "",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                } else null
            )
        }

        if (showBottomSheet) {
            GenCanvasBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { showBottomSheet = false }
            ) {
                GenCanvasBottomSheetItem(
                    text = stringResource(id = CoreR.string.core_action_take_photo),
                    showDivider = true,
                    onClick = { onTakePhoto() }
                )

                GenCanvasBottomSheetItem(
                    text = stringResource(id = CoreR.string.core_action_pick_from_gallery),
                    onClick = { onPickFromGallery() }
                )
            }
        }
    }
}