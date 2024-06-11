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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.work.WorkInfo
import com.example.blurimage.BlurImageApplication
import com.example.blurimage.KEY_IMAGE_URI
import com.example.blurimage.data.BlurImageRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * [BlurViewModel] starts and stops the WorkManger and applies blur to the image. Also updates the
 * visibility states of the buttons depending on the states of the WorkManger.
 */
class BlurViewModel(private val blurImageRepository: BlurImageRepository) : ViewModel() {
    val blurUiState: StateFlow<BlurUiState> = blurImageRepository.outputWorkInfo.map {
        info ->
        val outputImageUri = info.outputData.getString(KEY_IMAGE_URI)
        when {
            info.state.isFinished && !outputImageUri.isNullOrEmpty() -> {
                BlurUiState.Complete(outputUri = outputImageUri)
            }
            info.state == WorkInfo.State.CANCELLED -> {
                BlurUiState.Default
            }
            else -> BlurUiState.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BlurUiState.Default
    )
    /**
     * Call the method from repository to create the WorkRequest to apply the blur
     * and save the resulting image
     * @param blurLevel The amount to blur the image
     */
    fun applyBlur(blurLevel: Int, uriStr: String) {
        blurImageRepository.applyBlur(blurLevel, uriStr)
    }

    /**
     * Factory for [BlurViewModel] that takes [BlurImageRepository] as a dependency
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val blurImageRepository =
                    (this[APPLICATION_KEY] as BlurImageApplication).container.blurImageRepository
                BlurViewModel(
                    blurImageRepository = blurImageRepository
                )
            }
        }
    }
}

sealed interface BlurUiState {
    object Default : BlurUiState
    object Loading : BlurUiState
    data class Complete(val outputUri: String) : BlurUiState
}