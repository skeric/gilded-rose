package com.miw.gildedrose.service;

import com.miw.gildedrose.dto.ItemViewDto;
import com.miw.gildedrose.model.ItemView;
import com.miw.gildedrose.repository.ItemViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ViewCountService {
    private final ConversionService conversionService;
    private final ItemViewRepository itemViewRepository;

    public void addViewCount(ItemViewDto dto) {
        ItemView itemView = conversionService.convert(dto, ItemView.class);

        if (itemView == null) {
            return;
        }

        itemViewRepository.save(itemView);
    }
}
