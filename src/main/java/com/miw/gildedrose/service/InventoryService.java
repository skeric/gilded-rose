package com.miw.gildedrose.service;

import com.miw.gildedrose.dto.ItemDto;
import com.miw.gildedrose.model.Item;
import com.miw.gildedrose.model.ItemStock;
import com.miw.gildedrose.repository.ItemRepository;
import com.miw.gildedrose.repository.ItemStockRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {
    private final Integer pageSize;
    private final ConversionService conversionService;
    private final ItemRepository itemRepository;
    private final ItemStockRepository itemStockRepository;

    public InventoryService(@Value("${findAllItems.pageSize}") Integer pageSize,
                            ConversionService conversionService,
                            ItemRepository itemRepository,
                            ItemStockRepository itemStockRepository) {
        this.pageSize = pageSize;
        this.conversionService = conversionService;
        this.itemRepository = itemRepository;
        this.itemStockRepository = itemStockRepository;
    }

    public Optional<ItemDto> findItem(String name) {
        Item item = itemRepository.findByName(name);
        ItemDto dto = conversionService.convert(item, ItemDto.class);
        return (dto == null) ? Optional.empty() : Optional.of(dto);
    }

    // TODO implement a generic cast checking
    @SuppressWarnings("unchecked")
    public List<ItemDto> findAll() {
        // Since it is a demonstration of pagination, the page size is set to high number
        // such that all items are fetched.
        Pageable pageWithAllItems = PageRequest.of(0, pageSize);
        Page<Item> page = itemRepository.findAll(pageWithAllItems);
        List<Item> source = page.getContent();
        TypeDescriptor sourceType = TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Item.class));
        TypeDescriptor targetType = TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(ItemDto.class));
        List<ItemDto> dtos = (List<ItemDto>) conversionService.convert(source, sourceType, targetType);
        return dtos;
    }

    public Integer findQuantityForSell(Integer itemId) {
        Optional<ItemStock> found = itemStockRepository.findById(itemId);
        return found.isEmpty() ? 0 : found.get().getQuantityForSell();
    }
}
