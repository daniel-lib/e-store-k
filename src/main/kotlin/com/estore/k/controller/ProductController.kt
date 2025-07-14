package com.estore.k.controller

import com.estore.k.model.Product
import com.estore.k.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@Controller
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService
) {


    @GetMapping()
    fun getAllProducts(model: Model): String {
        val products: List<Product> = productService.getAllProductsWithVariants()
        model.addAttribute("products", products)
        return "layout/product-list-table"
    }

    @PostMapping()
    fun saveProduct(@ModelAttribute product: Product): ResponseEntity<String> {
        if (!product.title.isNullOrBlank() && !product.vendor.isNullOrBlank()) {
            productService.saveProduct(product)
            return ResponseEntity.ok("Product saved")
        } else {
            return ResponseEntity.badRequest().body("Invalid data")
        }
    }
    @GetMapping("/edit/{productId}")
    fun editProductView(@PathVariable("productId") productId: String, model: Model):String {
        var product: Product? = productService.getProductById(productId)
        //:return "layout/product-edit"
        model.addAttribute("product", product)
        return "product-edit"
    }
}
