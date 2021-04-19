package com.cursor.onlineshop.dtos;

import com.cursor.onlineshop.entities.goods.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private String categoryId;
    private String name;
    private String description;
    private String image;

    public CategoryDto(String name, String description, String image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public Category toEntity() {
        return new Category(categoryId, name, description, image);
    }
}
