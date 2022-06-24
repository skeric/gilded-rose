package com.miw.gildedrose.service;

import com.miw.gildedrose.exception.BadRequestException;
import com.miw.gildedrose.model.ItemStock;
import com.miw.gildedrose.repository.ItemStockRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class StockServiceTest {
    @MockBean
    private ItemStockRepository itemStockRepositoryMock;

    @Autowired
    private StockService stockService;

    private Integer id;
    private Integer itemId;
    private Integer currentQuantityForSell;
    private ItemStock currentItemStock;

    @BeforeEach
    void setup() {
        id = 1;
        itemId = 2;
        currentQuantityForSell = 10;
        currentItemStock = new ItemStock(1, itemId, currentQuantityForSell);
    }
    @SneakyThrows
    @Test
    @DisplayName("Update quantity successfully")
    void updateQuantity() {
        // given
        Mockito.when(itemStockRepositoryMock.findAndLock(currentItemStock.getItemId())).thenReturn(currentItemStock);
        ArgumentCaptor<ItemStock> argumentCaptorItemStockToBeSaved = ArgumentCaptor.forClass(ItemStock.class);

        // when
        Integer requestedQuantity = 5;
        stockService.updateQuantity(itemId, requestedQuantity);

        // then
        verify(itemStockRepositoryMock).save(argumentCaptorItemStockToBeSaved.capture());
        Integer quantityToBeUpdated = currentItemStock.getQuantityForSell() - requestedQuantity;
        assertEquals(new ItemStock(id, itemId, quantityToBeUpdated), argumentCaptorItemStockToBeSaved.getValue());
    }

    @Test
    @DisplayName("Fail to update quantity due to requestedQuantity is greater thant stock level")
    void failToUpdateQuantity() {
        // given
        Mockito.when(itemStockRepositoryMock.findAndLock(currentItemStock.getItemId())).thenReturn(currentItemStock);

        // when
        Integer requestedQuantity = 11;

        BadRequestException thrown = assertThrows(BadRequestException.class, () -> {
            stockService.updateQuantity(itemId, requestedQuantity);
        });

        // then
        assertEquals(String.format(StockService.NOT_ENOUGH_QUANTITY, currentQuantityForSell, requestedQuantity), thrown.getMessage());
    }
}