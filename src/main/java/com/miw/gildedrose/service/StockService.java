package com.miw.gildedrose.service;

import com.miw.gildedrose.exception.BadRequestException;
import com.miw.gildedrose.model.ItemStock;
import com.miw.gildedrose.repository.ItemStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StockService {
    public static final String NOT_ENOUGH_QUANTITY = "Quantity for sell is %d less than requested quantity %d";

    private final ItemStockRepository itemStockRepository;

    public void updateQuantity(Integer itemId, Integer requestedQuantity) throws BadRequestException {
        ItemStock found = itemStockRepository.findAndLock(itemId);

        if (found.getQuantityForSell() < requestedQuantity) {
            throw new BadRequestException(String.format(NOT_ENOUGH_QUANTITY, found.getQuantityForSell(), requestedQuantity));
        }

        ItemStock itemStockToBeSaved = new ItemStock(found.getId(), itemId, found.getQuantityForSell() - requestedQuantity);
        itemStockRepository.save(itemStockToBeSaved);
    }
}
