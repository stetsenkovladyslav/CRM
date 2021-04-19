package com.cursor.onlineshop.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateOrderItemDto {
    private String itemId;
    private int quantity;
}
