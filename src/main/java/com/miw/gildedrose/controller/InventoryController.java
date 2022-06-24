package com.miw.gildedrose.controller;

import com.miw.gildedrose.dto.ItemDto;
import com.miw.gildedrose.facade.InventoryFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("inventory")
public class InventoryController {
    private final InventoryFacade inventoryFacade;

    @GetMapping(value = "/items", produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<List<ItemDto>> getItems() {
        List<ItemDto> dtos = inventoryFacade.finalAll();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping(value = "/item/{name}", produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<ItemDto> getItem(@PathVariable String name) {
        Optional<ItemDto> dto = inventoryFacade.findItemWithLatestPrice(name);
        return (dto.isEmpty()) ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto.get());
    }

}
