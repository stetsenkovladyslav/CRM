package com.cursor.onlineshop.entities.orders;

import com.cursor.onlineshop.entities.goods.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @Column(name = "order_item_id")
    private String orderItemId;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Item item;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;
    private int quantity;
    private BigDecimal price;

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemId='" + orderItemId + '\'' +
                ", item=" + item +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
