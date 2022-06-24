package com.miw.gildedrose.converter;

import com.miw.gildedrose.dto.ItemDto;
import com.miw.gildedrose.model.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.miw.gildedrose.helper.AssertionHelper.assertItemAndItemDto;

public class Item2ItemDtoTest {
    private final Item2ItemDto converter = new Item2ItemDto();

    @Test
    @DisplayName("Test successful conversion")
    void convertSuccessfully() {
        // given
        Item source = new Item(1, "Bronze mirror", "Old mirror", 100.12f);

        // when
        ItemDto target = converter.convert(source);

        // then
        assertItemAndItemDto(source, target);
    }
}
