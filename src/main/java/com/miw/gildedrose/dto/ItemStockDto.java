package com.miw.gildedrose.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@Builder
public class ItemStockDto {
    private final Integer id;
    private final Integer itemId;
    private final Integer quantityForSell;
}
