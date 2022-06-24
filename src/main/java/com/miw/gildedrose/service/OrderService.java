package com.miw.gildedrose.service;

import com.miw.gildedrose.dto.ItemDto;
import com.miw.gildedrose.dto.OrderDto;
import com.miw.gildedrose.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OrderService {
    public static final String ITEM_NOT_FOUND = "Item %s is not found";
    public static final String NOT_ENOUGH_QUANTITY = "Quantity for sell is %d less than requested quantity %d";

    private final InventoryService inventoryService;
    private final StockService stockService;

    @Transactional
    public OrderDto order(OrderDto request, Integer itemId) throws BadRequestException {
        stockService.updateQuantity(itemId, request.getQuantity());
        return OrderDto.builder()
                .orderNumber(UUID.randomUUID().toString())
                .name(request.getName())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .build();
    }

    public ItemDto findItem(String name) throws BadRequestException {
        Optional<ItemDto> found = inventoryService.findItem(name);

        if (found.isEmpty()) {
            throw new BadRequestException(String.format(ITEM_NOT_FOUND, name));
        }

        return found.get();
    }

    public void checkQuantity(Integer itemId, Integer requestedQuantity) throws BadRequestException {
        Integer quantity = inventoryService.findQuantityForSell(itemId);

        if (quantity < requestedQuantity) {
            throw new BadRequestException(String.format(NOT_ENOUGH_QUANTITY, quantity, requestedQuantity));
        }
    }

}
