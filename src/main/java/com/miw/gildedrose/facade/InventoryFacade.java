package com.miw.gildedrose.facade;

import com.miw.gildedrose.dto.ItemDto;
import com.miw.gildedrose.dto.ItemViewDto;
import com.miw.gildedrose.service.InventoryService;
import com.miw.gildedrose.service.SurgePriceService;
import com.miw.gildedrose.service.ViewCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class InventoryFacade {
    private final InventoryService inventoryService;
    private final SurgePriceService surgePriceService;
    private final ViewCountService viewCountService;

    public List<ItemDto> finalAll() {
        return inventoryService.findAll();
    }

    public Optional<ItemDto> findItemWithLatestPrice(String name) {
        Optional<ItemDto> dto = inventoryService.findItem(name);

        if (dto.isEmpty()) {
            return dto;
        }

        Float latestPrice = surgePriceService.adjustPrice(dto.get().getId(), dto.get().getPrice());

        // update view count after finding the latest price.
        viewCountService.addViewCount(ItemViewDto.builder().itemId(dto.get().getId()).build());

        return Optional.of(ItemDto.builder()
                .id(dto.get().getId())
                .name(name)
                .description(dto.get().getDescription())
                .price(latestPrice)
                .build());
    }
}
