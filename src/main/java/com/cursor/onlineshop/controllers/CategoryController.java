package com.cursor.onlineshop.controllers;

import com.cursor.onlineshop.dtos.CategoryDto;
import com.cursor.onlineshop.dtos.CreateCategoryDto;
import com.cursor.onlineshop.entities.goods.Category;
import com.cursor.onlineshop.services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/categories")
@Secured("ROLE_ADMIN")
public class CategoryController {

    private final CategoryService categoryService;

    // 1. Add the following params here: limit (10 by default), offset (0 by default) and sort (by category name by default)
    // 2. If everything is fine return code 200, not 302. Code 302 is redirecting
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    // return code 200, not 302. HTTP status OK, not HTTP status FOUND
    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable String categoryId) {
        return new ResponseEntity<>(categoryService.getById(categoryId), HttpStatus.FOUND);
    }

    // use something like this return ResponseEntity.created().body();
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CreateCategoryDto newCategoryDto) {
        return new ResponseEntity<>(categoryService.add(newCategoryDto), HttpStatus.CREATED);
    }

    // use ResponseEntity.ok() instead
    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> editCategory(@PathVariable String categoryId, @RequestBody CategoryDto categoryDto) {
        categoryDto.setCategoryId(categoryId);
        return new ResponseEntity<>(categoryService.update(categoryDto), HttpStatus.OK);
    }

    // use ResponseEntity.ok() instead
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable String categoryId) {
        return new ResponseEntity<>(categoryService.delete(categoryId), HttpStatus.OK);
    }
}
