package com.cursor.onlineshop.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderItemDto {
    private String orderItemId;
    private String itemId;
    private int quantity;
}
