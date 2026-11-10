package com.example.consecutivepractices

import com.example.consecutivepractices.domain.model.Product
import com.example.consecutivepractices.domain.model.ProductFilter
import com.example.consecutivepractices.domain.model.Rating
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductFilterTest {
    @Test
    fun activeWhenEitherSettingChanges() {
        assertFalse(ProductFilter().hasActiveFilters)
        assertTrue(ProductFilter(category = "electronics").hasActiveFilters)
        assertTrue(ProductFilter(minRating = 3f).hasActiveFilters)
    }

    @Test
    fun matchesCategoryAndIncludesMinimumRating() {
        val filter = ProductFilter(category = "electronics", minRating = 3f)
        val product = Product(1, "SSD", 50.0, "", "electronics", "", Rating(3.0, 10))

        assertTrue(filter.matches(product))
        assertFalse(filter.matches(product.copy(category = "jewelery")))
        assertFalse(filter.matches(product.copy(rating = Rating(2.9, 10))))
    }
}
