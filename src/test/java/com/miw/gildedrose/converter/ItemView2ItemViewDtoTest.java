package com.miw.gildedrose.converter;

import com.miw.gildedrose.dto.ItemViewDto;
import com.miw.gildedrose.model.ItemView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ItemView2ItemViewDtoTest {
    private final ItemView2ItemViewDto converter = new ItemView2ItemViewDto();

    @Test
    @DisplayName("Test successful conversion")
    void convertSuccessfully() {
        // given
        ItemView source = new ItemView(1, 10, Timestamp.from(Instant.now()));

        // when
        ItemViewDto target = converter.convert(source);

        // then
        assertNotNull(target);
        assertEquals(source.getId(), target.getId());
        assertEquals(source.getItemId(), target.getItemId());
        assertEquals(source.getCreatedAt(), target.getCreatedAt());
    }
}
