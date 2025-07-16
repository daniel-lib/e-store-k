package com.estore.k.controller

import com.estore.k.model.Product
import com.estore.k.model.Variant
import com.estore.k.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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
            product.variants?.forEach { variant ->
                variant.productId = product.id
                productService.saveVariant(variant)
            }
            return ResponseEntity.ok("Product saved")
        } else {
            return ResponseEntity.badRequest().body("Invalid data")
        }
    }

    @GetMapping("/add")
    fun serveAddProductView(model: Model): String {
//        val product = Product()
//        product.variants = emptyList()
//        model.addAttribute("product", product)
        return "layout/add-product-form"
    }

    @GetMapping("/edit/{productId}")
    fun editProductView(@PathVariable("productId") productId: String, model: Model): String {
        var product: Product? = productService.getProductById(productId)
        var productVariant: List<Variant>? = productService.getVariantsByProductId(productId) ?: emptyList()
        product?.variants = productVariant
        model.addAttribute("product", product)
        return "product-edit"
    }

    @PostMapping("/update")
    fun updateProduct(@ModelAttribute product: Product): String {
        try {
            productService.updateProduct(product)
            return "redirect:/?success=true&message=product was updated successfully"
        } catch (ex: Exception) {
            ex.printStackTrace()
            return "redirect:/products/edit/${product.id}?error=true&message=product was not updated"
        }
    }

    @GetMapping("/delete/{productId}")
    @ResponseBody
    fun deleteProduct(@PathVariable("productId") productId: String): String {
        try {

            if (productService.deleteProduct(productId) == 0) return "error"
            productService.deleteProductVatiants(productId)
//            if (productService.deleteProductVatiants(productId) == 0) return ""
            return "success"
        } catch (ex: Exception) {
            ex.printStackTrace()
            return "error"
        }
    }

    @GetMapping("/search")
    fun servesearchPage(model: Model): String {
        val searchedProducts: List<Product> = emptyList()
        model.addAttribute("searchedProducts", searchedProducts)
        return "product-search"
    }

    @GetMapping("/search/")
    fun searchProducts(@RequestParam("q") query: String, model: Model): String {
        val searchedProducts = productService.searchProducts(query)

        model.addAttribute("searchedProducts", searchedProducts)
        model.addAttribute("searchQuery", query);
        return "layout/searched-products-table :: searched-products"

    }
}
