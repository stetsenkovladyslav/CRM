package com.cursor.onlineshop.dtos;

import com.cursor.onlineshop.entities.goods.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryDto {
    private String name;
    private String description;
    private String image;

    public Category toEntity() {
        return new Category(name, description, image);
    }
}
