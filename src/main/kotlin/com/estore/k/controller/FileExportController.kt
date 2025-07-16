package com.estore.k.controller

import com.estore.k.service.ProductService
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.nio.charset.StandardCharsets

@Controller
@RequestMapping("/export")
class FileExportController(private val productService: ProductService) {

    @GetMapping("/searched/products/csv")
    fun exportCsv(@RequestParam("query") query: String): ResponseEntity<ByteArray> {
        val products = productService.searchProducts(query)
        println("!!!!!working? ${products?.size}")
        println("QUERY++ ${query}")

        val csvHeader = "ID,Title,Handle,Vendor,Image\n"
        val csvBody = products?.joinToString("\n") {
            listOf(
                it.id.orEmpty(),
                it.title.orEmpty(),
                it.handle.orEmpty(),
                it.vendor.orEmpty(),
                it.image.orEmpty()
            ).joinToString(",") { value ->
                // Escape commas and quotes
                "\"${value.replace("\"", "\"\"")}\""
            }
        }

        val csvContent = (csvHeader + csvBody).toByteArray(StandardCharsets.UTF_8)

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=products.csv")
            .contentType(MediaType.parseMediaType("text/csv"))
            .body(csvContent)
    }
}