/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.blurimage.ui

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.blurimage.R

@Composable
fun BlurImageScreen(blurViewModel: BlurViewModel = viewModel(factory = BlurViewModel.Factory)) {
    val uiState by blurViewModel.blurUiState.collectAsStateWithLifecycle()
    val layoutDirection = LocalLayoutDirection.current
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(
                start = WindowInsets.safeDrawing
                    .asPaddingValues()
                    .calculateStartPadding(layoutDirection),
                end = WindowInsets.safeDrawing
                    .asPaddingValues()
                    .calculateEndPadding(layoutDirection)
            )
    ) {
        BlurImageScreenContent(
            blurUiState = uiState,
            applyBlur = blurViewModel::applyBlur,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}

@Composable
fun BlurImageScreenContent(
    blurUiState: BlurUiState,
    applyBlur: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedValue by rememberSaveable { mutableStateOf(3) }
    val context = LocalContext.current
    var uri by remember { mutableStateOf<Uri?>(null) }
    val singlePicPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri = it }
    )
    if (!context.areNotificationsEnabled()) {
        noNotificationAction(context)
    }
    Column(modifier = modifier) {
        Button (
            onClick = {
                singlePicPicker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        ) {
            Text( text = "Open Gallery" )
        }
        AsyncImage(
            model = uri,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            contentScale = ContentScale.Fit,
        )
        BlurActions(
            blurUiState = blurUiState,
            onStartClick = { applyBlur(selectedValue, uri.toString()) },
            ctx = context,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
private fun BlurActions(
    blurUiState: BlurUiState,
    onStartClick: () -> Unit,
    ctx: Context,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        when (blurUiState) {
            is BlurUiState.Default -> {
                Button(
                    onClick = onStartClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(id = R.string.start))
                }
            }
            is BlurUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.padding(
                        dimensionResource(id = R.dimen.padding_small)
                    )
                )
            }
            is BlurUiState.Complete -> {
                Button(
                    onClick = onStartClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(id = R.string.start))
                }
                showBlurredImage(ctx, blurUiState.outputUri)
            }
        }
    }
}

fun Context.areNotificationsEnabled(): Boolean {
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    return notificationManager.areNotificationsEnabled()
}

private fun noNotificationAction(context: Context) {
    Toast.makeText(context, "No permission for notification", Toast.LENGTH_SHORT).show()
}

private fun showBlurredImage(context: Context, currentUri: String) {
    val uri = if (currentUri.isNotEmpty()) {
        Uri.parse(currentUri)
    } else {
        null
    }
    val actionView = Intent(Intent.ACTION_VIEW, uri)
    context.startActivity(actionView)
}
