package com.miw.gildedrose.converter;

import com.miw.gildedrose.dto.ItemViewDto;
import com.miw.gildedrose.model.ItemView;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ItemView2ItemViewDto implements Converter<ItemView, ItemViewDto> {
    @Override
    public ItemViewDto convert(ItemView source) {
        return ItemViewDto.builder()
                .id(source.getId())
                .itemId(source.getItemId())
                .createdAt(source.getCreatedAt())
                .build();
    }
}
