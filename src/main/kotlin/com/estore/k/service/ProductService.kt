package com.estore.k.service

import com.estore.k.dto.ProductWrapper
import com.estore.k.model.Product
import com.estore.k.model.Variant
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.net.URL
import java.time.OffsetDateTime
import java.util.UUID

@Service
class ProductService(
    private val jdbcClient: JdbcClient,
    private val objectMapper: ObjectMapper
) {

    //    @Value("\${PRODUCTS_URL}")
    private var productUrl: String = "https://famme.no/products.json";

    // Runs once on startup
    @Scheduled(initialDelay = 0, fixedDelay = Long.MAX_VALUE)
    fun importProducts() {
        print("Importing products...k")
        try {
            val url = URL(productUrl)
            val wrapper: ProductWrapper = objectMapper.readValue(url, ProductWrapper::class.java)
            val products = wrapper.products ?: emptyList()

            for (i in 0 until minOf(products.size, 50)) {
                val product = products[i]

                if (!product.images.isNullOrEmpty()) {
                    product.image = product.images?.firstOrNull()?.src
                }

                saveProduct(product)

                product.variants?.forEach { variant ->
                    variant.productId = product.id
                    jdbcClient.sql(
                        """
                                    INSERT INTO variants (id, product_id, title, price, featured_image)
                                    VALUES (:id, :productId, :title, :price, :featuredImage)
                                    ON CONFLICT (id) DO NOTHING
                                    """
                    )
                        .param("id", variant.id)
                        .param("productId", variant.productId)
                        .param("title", variant.title)
                        .param("price", variant.price)
                        .param("featuredImage", variant.featuredImage)
                        .update()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getAllProductsWithVariants(): List<Product> {
        val products: List<Product> =
            jdbcClient.sql("SELECT id, title, handle, vendor, image FROM products ORDER BY created_at DESC")
                .query { rs, _ ->
                    Product(
                        id = rs.getString("id"),
                        title = rs.getString("title"),
                        handle = rs.getString("handle"),
                        vendor = rs.getString("vendor"),
                        image = rs.getString("image")
                    )
                }
                .list()

        if (products.isEmpty()) return products

        val variants: List<Variant> = jdbcClient.sql(
            "SELECT id, product_id, title, price, featured_image FROM variants"
        )
            .query { rs, _ ->
                Variant(
                    id = rs.getLong("id"),
                    title = rs.getString("title"),
                    price = rs.getBigDecimal("price"),
                    productId = rs.getString("product_id"),
                    featuredImage = rs.getString("featured_image")
                )
            }
            .list()

        val grouped: Map<String?, List<Variant>> = variants.groupBy { it.productId }

        products.forEach { product ->
            val productId = product.id
            val productVariants = grouped[productId] ?: emptyList()
            product.variants = productVariants
            val image = productVariants.firstOrNull()?.featuredImage
            if (image != null) {
                product.image = image
            }
        }

        return products
    }

    fun saveProduct(product: Product) {
        if (product.id == null) product.id = UUID.randomUUID().toString()
        if (product.image == null) product.image =
            "https://images.unsplash.com/photo-1701769454078-2ba2f3788bc2?q=80&w=423&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
        if (product.createdAt == null) product.createdAt = OffsetDateTime.now();
        jdbcClient.sql(
            """
                                INSERT INTO products (id, title, handle, vendor, image, created_at)
                                VALUES (:id, :title, :handle, :vendor, :image, :createdAt)
                                ON CONFLICT (id) DO NOTHING
                                """
        )
            .param("id", product.id)
            .param("title", product.title)
            .param("handle", product.handle)
            .param("vendor", product.vendor)
            .param("image", product.image)
            .param("createdAt", product.createdAt)
            .update()
    }
}
