package com.example.nutristride.ui.screens.food

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nutristride.data.model.FoodItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodSearchScreen(
    onBackClick: () -> Unit,
    onFoodItemClick: (String) -> Unit,
    onAddManualClick: () -> Unit,
    onScanBarcodeClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var activeTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Search", "Recent", "Frequent", "My Meals", "Favorites")
    
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
                    contentDescription = "Add Food Manually",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Bar
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = { /* Perform search */ },
                active = false,
                onActiveChange = { },
                placeholder = { Text("Search for food...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                trailingIcon = {
                    IconButton(onClick = onScanBarcodeClick) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
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
                    searchResults = emptyList(), // Replace with actual search results
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
fun SearchResultsList(
    searchResults: List<FoodItem>,
    onFoodItemClick: (String) -> Unit
) {
    if (searchResults.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
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

@Composable
fun FoodItemRow(
    foodItem: FoodItem,
    onClick: () -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = foodItem.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (foodItem.brand != null) {
                        Text(
                            text = foodItem.brand,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                
                Text(
                    text = "${foodItem.calories} cal",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                if (trailingIcon != null) {
                    trailingIcon()
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Protein: ${foodItem.protein}g",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Carbs: ${foodItem.carbs}g",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Fat: ${foodItem.fat}g",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
