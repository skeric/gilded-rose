package com.miw.gildedrose.controller;

import com.miw.gildedrose.facade.InventoryFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class InventoryControllerTest {
    @MockBean
    private InventoryFacade inventoryFacadeMock;

    @Test
    @DisplayName("find all items successfully")
    void findAllItems() {
        // TODO implementation
        assertTrue(true);
    }

    @Test
    @DisplayName("find an item by name")
    void findItemByName() {
        // TODO implementation
        assertTrue(true);
    }

    @Test
    @DisplayName("not able to find an item by name")
    void itemNotFound() {
        // TODO implementation
        assertTrue(true);
    }
}
