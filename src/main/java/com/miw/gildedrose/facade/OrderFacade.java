package com.miw.gildedrose.facade;

import com.miw.gildedrose.dto.ItemDto;
import com.miw.gildedrose.dto.OrderDto;
import com.miw.gildedrose.exception.BadRequestException;
import com.miw.gildedrose.service.OrderService;
import com.miw.gildedrose.service.SurgePriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderFacade {
    private final OrderService orderService;
    private final SurgePriceService surgePriceService;

    public OrderDto order(OrderDto request) throws BadRequestException {
        ItemDto itemDto = orderService.findItem(request.getName());
        orderService.checkQuantity(itemDto.getId(), request.getQuantity());

        Float latestPrice = surgePriceService.adjustPrice(itemDto.getId(), itemDto.getPrice());

        // Build order with updated price
        OrderDto requestWithPrice = OrderDto.builder()
                .name(request.getName())
                .price(latestPrice)
                .quantity(request.getQuantity())
                .build();

        return orderService.order(requestWithPrice, itemDto.getId());
    }

}
