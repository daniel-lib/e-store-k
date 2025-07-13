package com.estore.k.model

import com.estore.k.dto.Image
import java.io.Serializable
import java.time.OffsetDateTime

data class Product(
        var id: String? = null,
        var title: String? = null,
        var handle: String? = null,
        var variants: List<Variant>? = emptyList(),
        var vendor: String? = null,
        var image: String? = null,
        var images: List<Image>? = null,
        var createdAt: OffsetDateTime? = null
) : Serializable
