package com.miw.gildedrose.service;

import com.google.common.collect.ImmutableList;
import com.miw.gildedrose.dto.ItemDto;
import com.miw.gildedrose.helper.AssertionHelper;
import com.miw.gildedrose.model.Item;
import com.miw.gildedrose.model.ItemStock;
import com.miw.gildedrose.repository.ItemRepository;
import com.miw.gildedrose.repository.ItemStockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class InventoryServiceTest {
    @MockBean
    private ItemRepository itemRepositoryMock;

    @MockBean
    private ItemStockRepository itemStockRepositoryMock;

    private InventoryService inventoryService;

    @Autowired
    private ConversionService conversionService;

    private static final Integer PAGE_SIZE = 100;

    private Item itemOne;
    private List<Item> items;

    @BeforeEach
    void init() {
        inventoryService = new InventoryService(PAGE_SIZE, conversionService, itemRepositoryMock, itemStockRepositoryMock);
        itemOne = new Item(1, "Fisheye Mirror", "Mirror", 100.00f);
        Item itemTwo = new Item(2, "Old Typewriter", "Office supplies", 50.00f);
        items = ImmutableList.of(itemOne, itemTwo);
    }

    @Test
    @DisplayName("Find item successfully")
    void findItemSuccessfully() {
        // given
        when(itemRepositoryMock.findByName(itemOne.getName())).thenReturn(itemOne);

        // when
        Optional<ItemDto> actual = inventoryService.findItem(itemOne.getName());

        // then
        ItemDto expected = ItemDto.builder()
                .id(itemOne.getId())
                .name(itemOne.getName())
                .description(itemOne.getDescription())
                .price(itemOne.getPrice())
                .build();

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    @DisplayName("Fail to find an item")
    void failToFindItem() {
        // given

        // when
        Optional<ItemDto> actual = inventoryService.findItem(itemOne.getName());

        // then
        assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("Find all items in one page successfully")
    void findAllItemsSuccessfully() {
        // given
        Page<Item> pageWithFoundItems = new PageImpl<>(items);
        when(itemRepositoryMock.findAll(PageRequest.of(0, PAGE_SIZE))).thenReturn(pageWithFoundItems);

        // when
        List<ItemDto> dtos = inventoryService.findAll();

        // then
        assertEquals(items.size(), dtos.size());

        items.forEach((source) -> {
            List<ItemDto> targets = dtos.stream().filter(dto -> dto.getId().equals(source.getId()))
                    .collect(Collectors.toList());
            assertEquals(1, targets.size());
            AssertionHelper.assertItemAndItemDto(source, targets.get(0));
        });
    }

    @Test
    @DisplayName("Find quantity for sell for an item")
    void findItemQuantityForSell() {
        // given
        ItemStock itemStock = new ItemStock(100, itemOne.getId(), 50);
        when(itemStockRepositoryMock.findById(itemOne.getId())).thenReturn(Optional.of(itemStock));

        // when
        Integer foundQuantity = inventoryService.findQuantityForSell(itemOne.getId());

        // then
        assertEquals(itemStock.getQuantityForSell(), foundQuantity);
    }
}
