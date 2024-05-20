package com.example.attractions.data

import com.example.attractions.model.Attraction

data class AttractionUiState(
    val curAttraction: Attraction = Attraction(0, 0)
)