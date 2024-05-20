package com.example.attractions.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Attraction(
    @StringRes val stringResourceId: Int,
    @DrawableRes val imageResourceId: Int
)