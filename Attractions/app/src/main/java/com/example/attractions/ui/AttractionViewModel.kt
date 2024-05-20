package com.example.attractions.ui

import androidx.lifecycle.ViewModel
import com.example.attractions.data.AttractionUiState
import com.example.attractions.model.Attraction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AttractionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AttractionUiState())
    val uiState: StateFlow<AttractionUiState> = _uiState.asStateFlow()

    fun updateCurAttraction(newAttraction: Attraction) {
        _uiState.update { currentState ->
            currentState.copy(
                curAttraction = newAttraction
            )
        }
    }
}