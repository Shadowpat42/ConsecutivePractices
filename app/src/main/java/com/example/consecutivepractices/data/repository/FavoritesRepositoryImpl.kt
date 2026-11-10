package com.example.consecutivepractices.data.repository

import com.example.consecutivepractices.data.local.database.FavoriteProductDao
import com.example.consecutivepractices.data.local.database.FavoriteProductEntity
import com.example.consecutivepractices.domain.model.Product
import com.example.consecutivepractices.domain.model.Rating
import com.example.consecutivepractices.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(private val dao: FavoriteProductDao) : FavoritesRepository {
    override fun observeAll() = dao.observeAll().map { rows ->
        rows.map { row ->
            Product(row.id, row.title, row.price, row.description, row.category, row.image,
                Rating(row.rating, row.ratingCount))
        }
    }

    override suspend fun contains(id: Int) = dao.contains(id)

    override suspend fun setFavorite(product: Product, favorite: Boolean) {
        if (favorite) {
            dao.save(FavoriteProductEntity(
                id = product.id,
                title = product.title,
                price = product.price,
                description = product.description,
                category = product.category,
                image = product.image,
                rating = product.rating.rate,
                ratingCount = product.rating.count
            ))
        } else {
            dao.remove(product.id)
        }
    }
}
