package com.miw.gildedrose.facade;

import com.google.common.collect.ImmutableList;
import com.miw.gildedrose.dto.ItemDto;
import com.miw.gildedrose.service.InventoryService;
import com.miw.gildedrose.service.SurgePriceService;
import com.miw.gildedrose.service.ViewCountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class InventoryFacadeTest {
    @MockBean
    private SurgePriceService surgePriceServiceMock;
    @MockBean
    private ViewCountService viewCountServiceMock;
    @MockBean
    private InventoryService inventoryServiceMock;

    private InventoryFacade inventoryFacade;

    private List<ItemDto> itemDtos;

    @BeforeEach
    void init() {
        inventoryFacade = new InventoryFacade(inventoryServiceMock, surgePriceServiceMock, viewCountServiceMock);
        ItemDto itemDtoOne = ItemDto.builder()
                .id(1)
                .name("Fisheye Mirror")
                .description("Mirror")
                .price(100.00f)
                .build();
        ItemDto itemDtoTwo = ItemDto.builder()
                .id(2)
                .name("Old Typewriter")
                .description("Office Supplies")
                .price(50.00f)
                .build();
        itemDtos = ImmutableList.of(itemDtoOne, itemDtoTwo);
    }

    @Test
    @DisplayName("Find all items successfully")
    void findAll() {
        // given
        when(inventoryServiceMock.findAll()).thenReturn(itemDtos);

        // when
        List<ItemDto> found = inventoryFacade.finalAll();

        // then
        assertEquals(itemDtos, found);
    }

    @Test
    @DisplayName("Find an item with latest price successfully")
    void findItemWithLatestPrice() {
        // given
        ItemDto requestedItem = itemDtos.get(0);
        when(inventoryServiceMock.findItem(requestedItem.getName())).thenReturn(Optional.of(requestedItem));

        Float increasedPrice = 110.00f;
        when(surgePriceServiceMock.adjustPrice(requestedItem.getId(), requestedItem.getPrice()))
                .thenReturn(increasedPrice);

        // when
        Optional<ItemDto> actual = inventoryFacade.findItemWithLatestPrice(requestedItem.getName());

        // then
        assertTrue(actual.isPresent());
        assertEquals(requestedItem.getId(), actual.get().getId());
        assertEquals(requestedItem.getName(), actual.get().getName());
        assertEquals(requestedItem.getDescription(), actual.get().getDescription());
        assertEquals(increasedPrice, actual.get().getPrice());
    }

    @Test
    @DisplayName("Return empty result if an item cannot be found")
    void notFindAnItem() {
        // TODO requires implementation
        assertTrue(true);
    }
}
