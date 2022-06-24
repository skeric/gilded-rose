package com.miw.gildedrose.helper;

import com.miw.gildedrose.dto.ItemDto;
import com.miw.gildedrose.dto.OrderDto;
import com.miw.gildedrose.model.Item;
import com.miw.gildedrose.service.OrderService;
import org.apache.commons.lang3.StringUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssertionHelper {
    public static void assertItemAndItemDto(Item source, ItemDto target) {
        assertNotNull(target);
        assertEquals(source.getId(), target.getId());
        assertEquals(source.getName(), target.getName());
        assertEquals(source.getDescription(), target.getDescription());
        assertEquals(source.getPrice(), target.getPrice());
    }

    public static void assertPlacedOrder(OrderDto request, OrderDto result) {
        assertNotNull(result);
        assertNotNull(result.getOrderNumber());
        assertEquals(request.getName(), result.getName());
        assertEquals(request.getPrice(), result.getPrice());
        assertEquals(request.getQuantity(), result.getQuantity());
        assertTrue(StringUtils.isBlank(result.getErrorMessage()));
    }

    public static void assertFailedOrder(OrderDto request, Integer quantityForSell, OrderDto result) {
        assertNotNull(result);
        assertNull(result.getOrderNumber());
        assertEquals(request.getName(), result.getName());
        assertEquals(request.getPrice(), result.getPrice());
        assertEquals(request.getQuantity(), result.getQuantity());
        assertFailedOrderMessage(quantityForSell, request.getQuantity(),result.getErrorMessage());
    }

    public static void assertFailedOrderMessage(Integer quantityForSell, Integer requestedQuantity, String errorMessage) {
        assertEquals(String.format(OrderService.NOT_ENOUGH_QUANTITY, quantityForSell, requestedQuantity), errorMessage);
    }

}
