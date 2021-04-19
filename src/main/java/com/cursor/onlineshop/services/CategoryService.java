package com.cursor.onlineshop.services;

import com.cursor.onlineshop.dtos.CategoryDto;
import com.cursor.onlineshop.dtos.CreateCategoryDto;
import com.cursor.onlineshop.entities.goods.Category;
import com.cursor.onlineshop.repositories.CategoryRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@AllArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepo categoryRepo;

    public Category add(CreateCategoryDto newCategoryDto) {
        return categoryRepo.save(newCategoryDto.toEntity());
    }

    public Category update(CategoryDto updatedCategoryDto) {
        return categoryRepo.save(updatedCategoryDto.toEntity());
    }

    public String delete(String deletedCategoryId) {
        Category categoryToDelete = categoryRepo.findByCategoryId(deletedCategoryId).orElseThrow();
        categoryRepo.delete(categoryToDelete);
        return categoryRepo.findByCategoryId(deletedCategoryId).isPresent() ?
                String.format("Category with id=%s not deleted", deletedCategoryId) :
                String.format("Category with id=%s succesfully deleted", deletedCategoryId);
    }

    public Category getById(String categoryId) {
        return categoryRepo.findByCategoryId(categoryId).orElseThrow();
    }

    public List<Category> getAll() {
        return categoryRepo.findAll();
    }
}
