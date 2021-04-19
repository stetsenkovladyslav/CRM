package com.cursor.onlineshop.repositories;

import com.cursor.onlineshop.entities.goods.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// add @repo annotation
public interface CategoryRepo extends JpaRepository<Category, String> {
    Optional<Category> findByCategoryId(String categoryId);
}
