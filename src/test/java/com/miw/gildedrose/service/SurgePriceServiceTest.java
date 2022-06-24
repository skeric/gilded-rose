package com.miw.gildedrose.service;

import com.miw.gildedrose.repository.ItemViewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SurgePriceServiceTest {
    private static final int DURATION = 60;
    private static final int VIEW_COUNT_TRIGGER_ADJUSTMENT = 11;
    private static final int ADJUSTMENTPERCENT = 10;

    @MockBean
    private ItemViewRepository itemViewRepositoryMock;

    private SurgePriceService surgePriceService;

    @BeforeEach
    void init() {
        surgePriceService = new SurgePriceService(DURATION, VIEW_COUNT_TRIGGER_ADJUSTMENT, ADJUSTMENTPERCENT, itemViewRepositoryMock);
    }

    @Test
    @DisplayName("Increase the price by 10% due to view count increased by 11 within an hour")
    void increasePrice() {
        // given
        when(itemViewRepositoryMock.findViewCount(anyInt(), any(Timestamp.class), any(Timestamp.class)))
                .thenReturn(VIEW_COUNT_TRIGGER_ADJUSTMENT);
        Integer itemId = 1;
        Float basePrice = 100.25f;

        // when
        Float actual = surgePriceService.adjustPrice(itemId, basePrice);

        // then
        Float expected = 110.28f;
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Not increase the price if the view count is less than the triggering view count")
    void notIncreasePrice() {
        // given
        Integer viewCountLessThanTriggeringCount = VIEW_COUNT_TRIGGER_ADJUSTMENT - 1;
        when(itemViewRepositoryMock.findViewCount(anyInt(), any(Timestamp.class), any(Timestamp.class)))
                .thenReturn(viewCountLessThanTriggeringCount);
        Integer itemId = 1;
        Float basePrice = 100.25f;

        // when
        Float actual = surgePriceService.adjustPrice(itemId, basePrice);

        // then
        assertEquals(basePrice, actual);
    }


}
