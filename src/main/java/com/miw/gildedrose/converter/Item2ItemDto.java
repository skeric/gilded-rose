package com.miw.gildedrose.converter;

import com.miw.gildedrose.dto.ItemDto;
import com.miw.gildedrose.model.Item;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class Item2ItemDto implements Converter<Item, ItemDto> {
    @Override
    public ItemDto convert(Item source) {
        return ItemDto.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .price(source.getPrice())
                .build();
    }
}
