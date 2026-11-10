package com.example.consecutivepractices.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun ShopApp() {

    val navController = rememberNavController()

    val backStackEntry by
    navController.currentBackStackEntryAsState()

    val currentRoute =
        backStackEntry?.destination?.route

    val showBottomBar =
        currentRoute == "products" ||
                currentRoute == "profile" ||
                currentRoute == "favorites"

    Scaffold(
        bottomBar = {

            if (showBottomBar) {

                NavigationBar {

                    NavigationBarItem(
                        selected = currentRoute == "products",
                        onClick = {
                            navController.navigate("products") {
                                popUpTo("products")
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Text("🛍")
                        },
                        label = {
                            Text("Товары")
                        }
                    )

                    NavigationBarItem(
                        selected = currentRoute == "favorites",
                        onClick = {
                            if (currentRoute != "favorites") {
                                navController.navigate("favorites") {
                                    popUpTo("products")
                                    launchSingleTop = true
                                }
                            }
                        },
                        icon = { Text("★") },
                        label = { Text("Избранное") }
                    )

                    NavigationBarItem(
                        selected = currentRoute == "profile",
                        onClick = {
                            if (currentRoute != "profile") {
                                navController.navigate("profile") {
                                    popUpTo("products")
                                    launchSingleTop = true
                                }
                            }
                        },
                        icon = {
                            Text("👤")
                        },
                        label = {
                            Text("Профиль")
                        }
                    )
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "products",
            modifier = Modifier.padding(innerPadding)
        ) {

            composable("products") { entry ->
                val filtersSaved by entry.savedStateHandle
                    .getStateFlow("filtersSaved", false)
                    .collectAsState()

                ProductListScreen(
                    onProductClick = { id -> navController.navigate("details/$id") },
                    onFiltersClick = { navController.navigate("filters") },
                    filtersSaved = filtersSaved,
                    onFiltersHandled = { entry.savedStateHandle["filtersSaved"] = false }
                )
            }

            composable("filters") {
                FiltersScreen(
                    onDone = {
                        navController.previousBackStackEntry?.savedStateHandle?.set("filtersSaved", true)
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable("favorites") {
                FavoritesScreen()
            }

            composable("profile") {
                ProfileScreen()
            }

            composable("details/{productId}") { entry ->

                val productId =
                    entry.arguments
                        ?.getString("productId")
                        ?.toIntOrNull()

                if (productId != null) {

                    ProductDetailScreen(
                        productId = productId,
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}
