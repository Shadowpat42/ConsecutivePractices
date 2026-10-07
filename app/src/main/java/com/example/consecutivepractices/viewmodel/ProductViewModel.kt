package com.example.consecutivepractices.viewmodel

import androidx.lifecycle.ViewModel
import com.example.consecutivepractices.model.Product
import com.example.consecutivepractices.model.Rating

class ProductViewModel : ViewModel() {

    val products = listOf(
        Product(
            id = 1,
            title = "Городской рюкзак",
            price = 109.95,
            description = "Удобный рюкзак для города, учебы и небольших поездок.",
            category = "Мужская одежда",
            image = "",
            rating = Rating(
                rate = 3.9,
                count = 120
            )
        ),

        Product(
            id = 2,
            title = "Белая футболка",
            price = 22.30,
            description = "Простая хлопковая футболка на каждый день.",
            category = "Мужская одежда",
            image = "",
            rating = Rating(
                rate = 4.1,
                count = 259
            )
        ),

        Product(
            id = 3,
            title = "Осенняя куртка",
            price = 55.99,
            description = "Легкая куртка для прохладной погоды.",
            category = "Мужская одежда",
            image = "",
            rating = Rating(
                rate = 4.7,
                count = 500
            )
        ),

        Product(
            id = 4,
            title = "Серебряный браслет",
            price = 15.99,
            description = "Небольшой серебряный браслет.",
            category = "Украшения",
            image = "",
            rating = Rating(
                rate = 4.0,
                count = 430
            )
        ),

        Product(
            id = 5,
            title = "Золотое кольцо",
            price = 695.00,
            description = "Кольцо в классическом дизайне.",
            category = "Украшения",
            image = "",
            rating = Rating(
                rate = 4.6,
                count = 400
            )
        ),

        Product(
            id = 6,
            title = "Внешний SSD",
            price = 64.00,
            description = "Компактный накопитель для хранения файлов.",
            category = "Электроника",
            image = "",
            rating = Rating(
                rate = 3.3,
                count = 203
            )
        )
    )

    fun getProductById(id: Int): Product? {
        return products.find { product ->
            product.id == id
        }
    }
}