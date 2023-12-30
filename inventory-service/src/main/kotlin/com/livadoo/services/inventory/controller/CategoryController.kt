package com.livadoo.services.inventory.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.livadoo.services.inventory.data.Category
import com.livadoo.services.inventory.data.CategoryCreate
import com.livadoo.services.inventory.data.CategoryEdit
import com.livadoo.services.inventory.services.CategoryService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/categories")
class CategoryController(
    private val categoryService: CategoryService
) {

    @PostMapping
    suspend fun createCategory(
        @RequestPart("picture") filePart: FilePart,
        @RequestPart("category") category: String,
    ) {
        val categoryCreate = ObjectMapper().readValue(category, CategoryCreate::class.java)
        categoryService.createCategory(categoryCreate, filePart)
    }

    @PutMapping
    suspend fun updateCategory(@RequestBody categoryEdit: CategoryEdit) {
        categoryService.updateCategory(categoryEdit)
    }

    @PutMapping("/{categoryId}/picture", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun updateCategoryPicture(
        @PathVariable categoryId: String,
        @RequestPart("file") filePart: FilePart
    ): String {
        return categoryService.updateCategoryPicture(categoryId, filePart)
    }

    @GetMapping("/{categoryId}")
    suspend fun getCategory(@PathVariable categoryId: String): Category {
        return categoryService.getCategory(categoryId)
    }

    @DeleteMapping("/{categoryId}")
    suspend fun deleteCategory(@PathVariable categoryId: String) {
        categoryService.deleteCategory(categoryId)
    }

    @GetMapping
    suspend fun getCategories(
        @RequestParam("q", required = false) query: String?,
        @RequestParam("page", required = false) page: Int?,
        @RequestParam("size", required = false) size: Int?
    ): Page<Category> {
        val pageRequest = PageRequest.of(page ?: 0, size ?: 100)
        return categoryService.getCategories(pageRequest, query ?: "")
    }
}