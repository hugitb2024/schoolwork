package com.example.attractions.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.attractions.R
import com.example.attractions.model.Attraction

@Composable
fun AttractionScreen(attraction: Attraction, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = attraction.imageResourceId),
            contentDescription = stringResource(id = attraction.stringResourceId),
            modifier = Modifier
                .fillMaxWidth()
                .height(194.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            LocalContext.current.getString(attraction.stringResourceId)
        )
    }
}

@Preview
@Composable
private fun MyPreview() {
    AttractionScreen(Attraction(R.string.meiji_shrine, R.drawable.meiji_shrine))
}