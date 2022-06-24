package com.miw.gildedrose.converter;

import com.miw.gildedrose.dto.ItemViewDto;
import com.miw.gildedrose.model.ItemView;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ItemViewDto2ItemView implements Converter<ItemViewDto, ItemView> {
    @Override
    public ItemView convert(ItemViewDto source) {
        return new ItemView(source.getItemId());
    }
}
