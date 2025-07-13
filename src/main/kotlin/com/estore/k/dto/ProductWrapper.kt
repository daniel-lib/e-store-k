package com.estore.k.dto

import com.estore.k.model.Product

data class ProductWrapper(
        var products: List<Product>? = null
)
