package com.estore.k.controller

import com.estore.k.model.Product
import com.estore.k.service.ProductService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/")
class HomeController(private val productService: ProductService) {

    @GetMapping
    fun displayHomePage(model: Model): String {
        val products: List<Product> = productService.getAllProductsWithVariants()
        model.addAttribute("products", products)
        return "product-list"
    }
}