package com.miw.gildedrose.service;

import com.miw.gildedrose.dto.ItemDto;
import com.miw.gildedrose.dto.OrderDto;
import com.miw.gildedrose.exception.BadRequestException;
import com.miw.gildedrose.helper.AssertionHelper;
import com.miw.gildedrose.model.Item;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderServiceTest {
    @MockBean
    private InventoryService inventoryServiceMock;

    @MockBean
    private StockService stockServiceMock;

    private OrderService orderService;

    private Item itemOne;

    @BeforeEach
    void init() {
        itemOne = new Item(1, "Fisheye Mirror", "Mirror", 100.00f);
        orderService = new OrderService(inventoryServiceMock, stockServiceMock);
    }

    @SneakyThrows
    @Test
    @DisplayName("Order an item successfully")
    void orderSuccessfully() {
        // given
        Integer requestedQuantity = 1;

        // when
        OrderDto request = OrderDto.builder()
                .name(itemOne.getName())
                .price(itemOne.getPrice())
                .quantity(requestedQuantity)
                .build();
        OrderDto result = orderService.order(request, itemOne.getId());

        // then
        AssertionHelper.assertPlacedOrder(request, result);
    }

    @SneakyThrows
    @Test
    @DisplayName("Fail to order an item due to requested quantity is greater than the quantity for sell")
    void failToOrder() {
        // given
        Integer requestedQuantity = 2;
        Integer quantityForSell = 1;
        String errorMessage = String.format(OrderService.NOT_ENOUGH_QUANTITY, quantityForSell, requestedQuantity);
        doThrow(new BadRequestException(errorMessage)).when(stockServiceMock).updateQuantity(itemOne.getId(), requestedQuantity);

        // when
        OrderDto request = OrderDto.builder()
                .name(itemOne.getName())
                .price(itemOne.getPrice())
                .quantity(requestedQuantity)
                .build();

        BadRequestException thrown = assertThrows(BadRequestException.class, () -> {
            orderService.order(request, itemOne.getId());
        });

        // then
        AssertionHelper.assertFailedOrderMessage(quantityForSell, requestedQuantity, thrown.getMessage());
    }

    @SneakyThrows
    @Test
    @DisplayName("Find an item successfully")
    void findItemSuccessfully() {
        // given
        ItemDto expected = ItemDto.builder()
                .id(itemOne.getId())
                .name(itemOne.getName())
                .description(itemOne.getDescription())
                .price(itemOne.getPrice())
                .build();

        when(inventoryServiceMock.findItem(itemOne.getName())).thenReturn(Optional.of(expected));

        // when
        ItemDto actual = orderService.findItem(itemOne.getName());

        // then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Fail to find an item")
    void failToFindItem() {
        // given
        when(inventoryServiceMock.findItem(itemOne.getName())).thenReturn(Optional.empty());

        // when
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> {
            orderService.findItem(itemOne.getName());
        });

        // then
        assertEquals(String.format(OrderService.ITEM_NOT_FOUND, itemOne.getName()), thrown.getMessage());
    }

    @Test
    @DisplayName("Fail to check quantity if the requested quantity is greater than the quantity for sell")
    void failToCheckQuantity() {
        // given
        Integer quantityForSell = 1;
        when(inventoryServiceMock.findQuantityForSell(itemOne.getId())).thenReturn(quantityForSell);

        // when
        Integer requestedQuantity = 2;
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> {
            orderService.checkQuantity(itemOne.getId(), requestedQuantity);
        });

        // then
        assertEquals(String.format(OrderService.NOT_ENOUGH_QUANTITY, quantityForSell, requestedQuantity), thrown.getMessage());
    }
}