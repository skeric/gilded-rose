package com.miw.gildedrose.converter;

import com.miw.gildedrose.dto.ItemViewDto;
import com.miw.gildedrose.model.ItemView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ItemViewDto2ItemViewTest {
    private final ItemViewDto2ItemView converter = new ItemViewDto2ItemView();

    @Test
    @DisplayName("Test successful conversion")
    void convertSuccessfully() {
        // given
        ItemViewDto source = ItemViewDto.builder().itemId(2).build();

        // when
        ItemView target = converter.convert(source);

        // then
        assertNotNull(target);
        assertNull(target.getId());
        assertEquals(source.getItemId(), target.getItemId());
        assertNotNull( target.getCreatedAt());
    }
}
