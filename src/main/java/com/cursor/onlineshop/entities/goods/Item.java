package com.cursor.onlineshop.entities.goods;

import com.cursor.onlineshop.dtos.ItemDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Item {
    @Id
    @Column(name = "item_id")
    private String itemId;
    @Column(name = "item_name")
    private String name;
    @Column(name = "item_description")
    private String description;
    private BigDecimal price;
    @Column(name = "amount_in_stock")
    private int amountInStock;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Category category;

    public Item(String name, String description, BigDecimal price, int amountInStock, Category category) {
        this.itemId = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.price = price;
        this.amountInStock = amountInStock;
        this.category = category;
    }

    public ItemDto toDto() {
        return new ItemDto(itemId, name, description, price.doubleValue(), amountInStock, category.getCategoryId());
    }
}
