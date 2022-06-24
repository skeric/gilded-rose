package com.miw.gildedrose.controller;

import com.miw.gildedrose.facade.OrderFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class OrderControllerTest {
    @MockBean
    private OrderFacade orderFacadeMock;

    @Test
    @DisplayName("order an item successfully")
    void orderSuccessFully() {
        // TODO implementation
        assertTrue(true);
    }

    @Test
    @DisplayName("fail to order an item due to item not found")
    void failToOrderNotExistItem() {
        // TODO implementation
        assertTrue(true);
    }

    @Test
    @DisplayName("fail to order an item due to not enough quantity for sell")
    void failToOrderNotEnoughQuantityForSell() {
        // TODO implementation
        assertTrue(true);
    }
}
