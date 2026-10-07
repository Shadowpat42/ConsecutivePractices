package com.example.consecutivepractices.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.consecutivepractices.viewmodel.ProductViewModel

@Composable
fun ShopApp(
    productViewModel: ProductViewModel = viewModel()
) {

    val navController = rememberNavController()

    val backStackEntry by
    navController.currentBackStackEntryAsState()

    val currentRoute =
        backStackEntry?.destination?.route

    val showBottomBar =
        currentRoute == "products" ||
                currentRoute == "profile"

    Scaffold(
        bottomBar = {

            if (showBottomBar) {

                NavigationBar {

                    NavigationBarItem(
                        selected = currentRoute == "products",
                        onClick = {
                            navController.navigate("products") {
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
                        selected = currentRoute == "profile",
                        onClick = {
                            navController.navigate("profile") {
                                launchSingleTop = true
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

            composable("products") {

                ProductListScreen(
                    products = productViewModel.products,
                    onProductClick = { productId ->

                        navController.navigate(
                            "details/$productId"
                        )
                    }
                )
            }

            composable("profile") {

                ProfileScreen()
            }

            composable("details/{productId}") { entry ->

                val productId =
                    entry.arguments
                        ?.getString("productId")
                        ?.toIntOrNull()

                val product =
                    productId?.let {
                        productViewModel.getProductById(it)
                    }

                ProductDetailScreen(
                    product = product,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}