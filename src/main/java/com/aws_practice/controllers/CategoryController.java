package com.aws_practice.controllers;

import com.aws_practice.models.Category;
import com.aws_practice.response.ApiResponse;
import com.aws_practice.services.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/api/categories")
@RestController
public class CategoryController {
    private final ICategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return  ResponseEntity.ok(new ApiResponse("Found!", categories));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category name) {
        Category theCategory = categoryService.addCategory(name);
        return  ResponseEntity.ok(new ApiResponse("Success", theCategory));
    }

    @GetMapping("/category/{id}/category")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id){
        Category theCategory = categoryService.getCategoryById(id);
        return  ResponseEntity.ok(new ApiResponse("Found", theCategory));
    }

    @GetMapping("/category/{name}/category")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name){
        Category theCategory = categoryService.getCategoryByName(name);
        return  ResponseEntity.ok(new ApiResponse("Found", theCategory));
    }

    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategoryById(id);
        return  ResponseEntity.ok(new ApiResponse("Found", null));
    }

    @PutMapping("/category/{id}/update")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        Category updatedCategory = categoryService.updateCategory(category, id);
        return ResponseEntity.ok(new ApiResponse("Update success!", updatedCategory));
    }

}
