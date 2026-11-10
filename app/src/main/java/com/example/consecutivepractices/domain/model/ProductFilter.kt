package com.example.consecutivepractices.domain.model

data class ProductFilter(
    val category: String? = null,
    val minRating: Float = 0f
) {
    val hasActiveFilters: Boolean
        get() = category != null || minRating > 0f

    fun matches(product: Product): Boolean =
        (category == null || product.category == category) && product.rating.rate >= minRating
}
