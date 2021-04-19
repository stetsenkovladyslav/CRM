package com.cursor.onlineshop.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateOrderDto {
    private Set<CreateOrderItemDto> orderItemsDtos;
}
