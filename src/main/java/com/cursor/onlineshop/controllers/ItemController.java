package com.cursor.onlineshop.controllers;

import com.cursor.onlineshop.dtos.CreateItemDto;
import com.cursor.onlineshop.dtos.ItemDto;
import com.cursor.onlineshop.entities.goods.Item;
import com.cursor.onlineshop.services.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("items")
@Secured("ROLE_ADMIN")
public class ItemController {

    private final ItemService itemService;

    // add limit and offset
    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        return new ResponseEntity<>(itemService.getAll(), HttpStatus.FOUND);
    }

    // return status 200 not 302
    // return ResponseEntity.ok(itemService.getById(itemId));
    @GetMapping("/{itemId}")
    public ResponseEntity<Item> getItemById(@PathVariable String itemId) {
        return new ResponseEntity<>(itemService.getById(itemId), HttpStatus.FOUND);
    }

    // ResponseEntity.created()
    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody CreateItemDto newItemDto) {
        return new ResponseEntity<>(itemService.add(newItemDto), HttpStatus.CREATED);
    }

    // ResponseEntity.ok()
    @PutMapping("/{itemId}")
    public ResponseEntity<Item> editItem(@PathVariable String itemId, @RequestBody ItemDto itemDto) {
        itemDto.setItemId(itemId);
        return new ResponseEntity<>(itemService.update(itemDto), HttpStatus.OK);
    }

    // ResponseEntity.ok() instead of new ResponseEntity<>()
    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> deleteItem(@PathVariable String itemId) {
        return new ResponseEntity<>(itemService.delete(itemId), HttpStatus.OK);
    }
}
