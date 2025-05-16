package com.example.nutristride.ui.screens.food

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nutristride.data.model.FoodItem
import com.example.nutristride.ui.components.FoodItemRow
import com.example.nutristride.ui.components.NutrientInfo

@Composable
fun SearchResultsList(
    searchResults: List<FoodItem>,
    isLoading: Boolean,
    error: String?,
    onFoodItemClick: (String) -> Unit
) {
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (error != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Error",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    } else if (searchResults.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "No results found",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Try a different search term or add a food manually",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    } else {
        LazyColumn {
            items(searchResults) { foodItem ->
                FoodItemRow(
                    foodItem = foodItem,
                    onClick = { onFoodItemClick(foodItem.id) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodSearchScreen(
    viewModel: FoodSearchViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onFoodItemClick: (String) -> Unit,
    onAddManualClick: () -> Unit,
    onScanBarcodeClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var activeTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Search", "Recent", "Frequent", "My Meals", "Favorites")
    
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    // Perform search when query changes
    LaunchedEffect(searchQuery) {
        if (searchQuery.length >= 3) {
            viewModel.searchFood(searchQuery)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Food Search") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddManualClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Food Manually"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search Bar
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = { viewModel.searchFood(searchQuery) },
                active = false,
                onActiveChange = { },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                trailingIcon = {
                    IconButton(onClick = onScanBarcodeClick) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = "Scan Barcode"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Search suggestions would go here
            }
            
            // Tabs
            TabRow(selectedTabIndex = activeTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = activeTabIndex == index,
                        onClick = { activeTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }
            
            // Content based on selected tab
            when (activeTabIndex) {
                0 -> SearchResultsList(
                    searchResults = searchResults,
                    isLoading = isLoading,
                    error = error,
                    onFoodItemClick = onFoodItemClick
                )
                1 -> RecentFoodsList(
                    recentFoods = emptyList(), // Replace with actual recent foods
                    onFoodItemClick = onFoodItemClick
                )
                2 -> FrequentFoodsList(
                    frequentFoods = emptyList(), // Replace with actual frequent foods
                    onFoodItemClick = onFoodItemClick
                )
                3 -> MyMealsList(
                    myMeals = emptyList(), // Replace with actual meals
                    onFoodItemClick = onFoodItemClick
                )
                4 -> FavoritesList(
                    favorites = emptyList(), // Replace with actual favorites
                    onFoodItemClick = onFoodItemClick,
                    onToggleFavorite = { /* Toggle favorite status */ }
                )
            }
        }
    }
}

@Composable
fun RecentFoodsList(
    recentFoods: List<FoodItem>,
    onFoodItemClick: (String) -> Unit
) {
    if (recentFoods.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "No recent foods",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Foods you log will appear here",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    } else {
        LazyColumn {
            items(recentFoods) { foodItem ->
                FoodItemRow(
                    foodItem = foodItem,
                    onClick = { onFoodItemClick(foodItem.id) }
                )
            }
        }
    }
}

@Composable
fun FrequentFoodsList(
    frequentFoods: List<FoodItem>,
    onFoodItemClick: (String) -> Unit
) {
    if (frequentFoods.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "No frequent foods",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Foods you log often will appear here",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    } else {
        LazyColumn {
            items(frequentFoods) { foodItem ->
                FoodItemRow(
                    foodItem = foodItem,
                    onClick = { onFoodItemClick(foodItem.id) }
                )
            }
        }
    }
}

@Composable
fun MyMealsList(
    myMeals: List<FoodItem>,
    onFoodItemClick: (String) -> Unit
) {
    if (myMeals.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "No saved meals",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Create custom meals to quickly log multiple foods at once",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    } else {
        LazyColumn {
            items(myMeals) { foodItem ->
                FoodItemRow(
                    foodItem = foodItem,
                    onClick = { onFoodItemClick(foodItem.id) }
                )
            }
        }
    }
}

@Composable
fun FavoritesList(
    favorites: List<FoodItem>,
    onFoodItemClick: (String) -> Unit,
    onToggleFavorite: (FoodItem) -> Unit
) {
    if (favorites.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "No favorite foods",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Mark foods as favorites for quick access",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    } else {
        LazyColumn {
            items(favorites) { foodItem ->
                FoodItemRow(
                    foodItem = foodItem,
                    onClick = { onFoodItemClick(foodItem.id) },
                    trailingIcon = {
                        IconButton(onClick = { onToggleFavorite(foodItem) }) {
                            Icon(
                                imageVector = if (foodItem.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Toggle Favorite",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            }
        }
    }
}


