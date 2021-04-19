package com.cursor.onlineshop.repositories;

import com.cursor.onlineshop.entities.goods.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// add @repo annotation
public interface ItemRepo extends JpaRepository<Item, String> {

    Optional<Item> findByItemId(String itemId);
}
