package com.cursor.onlineshop.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private String itemId;
    private String name;
    private String description;
    private double price;
    private int amountInStock;
    private String categoryId;
}
