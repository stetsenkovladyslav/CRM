package com.cursor.onlineshop.entities.goods;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @Column(name = "category_id")
    private String categoryId;
    @Column(name = "category_name")
    private String name;
    @Column(name = "category_description")
    private String description;
    @Column(name = "category_image")
    private String image;

    public Category(String name, String description, String image) {
        this.categoryId = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.image = image;
    }
}
