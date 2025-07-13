package com.estore.k.model

import java.io.Serializable
import java.math.BigDecimal

data class Variant(
        var id: Long? = null,
        var title: String? = null,
        var price: BigDecimal? = null,
        var featuredImage: String? = null,
        var productId: String? = null
) : Serializable