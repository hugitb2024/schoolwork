package com.example.attractions.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import com.example.attractions.data.Datasource
import com.example.attractions.model.Attraction

@Composable
fun AttractionListScreen(
    attractionList: List<Attraction>,
    viewModel: AttractionViewModel,
    onClickAttraction: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(attractionList) {attraction ->
            AttractionCard(
                attraction = attraction,
                viewModel = viewModel,
                onClickAttraction = onClickAttraction,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun AttractionCard(
    attraction: Attraction,
    viewModel: AttractionViewModel,
    onClickAttraction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = attraction.imageResourceId),
                contentDescription = stringResource(id = attraction.stringResourceId),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(194.dp),
                contentScale = ContentScale.Crop
            )
            Button(
                onClick = {
                    viewModel.updateCurAttraction(attraction)
                    onClickAttraction()
                },
                modifier = Modifier.width(300.dp)
            ) {
                Text(LocalContext.current.getString(attraction.stringResourceId))
            }
        }
    }
}

@Preview
@Composable
private fun MyPreview() {
    AttractionListScreen(attractionList = Datasource().loadAttractions(), AttractionViewModel(), {})
}