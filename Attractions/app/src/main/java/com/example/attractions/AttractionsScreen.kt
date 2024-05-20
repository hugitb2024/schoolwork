package com.example.attractions

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.attractions.data.Datasource
import com.example.attractions.ui.AttractionListScreen
import com.example.attractions.ui.AttractionScreen
import com.example.attractions.ui.AttractionViewModel

enum class AttractionsScreen(@StringRes val title: Int) {
    AttractionsList(title = R.string.app_name),
    Attraction(title = R.string.attraction)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttractionAppBar(
    currentScreen: AttractionsScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AttractionApp(
    viewModel: AttractionViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AttractionsScreen.valueOf(
        backStackEntry?.destination?.route ?: AttractionsScreen.AttractionsList.name
    )
    Scaffold(
        topBar = {
            AttractionAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) {
//        innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        NavHost(
            navController = navController,
            startDestination = AttractionsScreen.AttractionsList.name,
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(rememberScrollState())
//                .padding(innerPadding)
        ) {
            composable(route = AttractionsScreen.AttractionsList.name) {
                AttractionListScreen(
                    attractionList = Datasource().loadAttractions(),
                    viewModel = viewModel,
                    onClickAttraction = {
                        navController.navigate(AttractionsScreen.Attraction.name)
                    }
                )
            }
            composable(route = AttractionsScreen.Attraction.name) {
                AttractionScreen(
                    attraction = uiState.curAttraction,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}