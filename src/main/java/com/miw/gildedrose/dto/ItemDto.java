package com.miw.gildedrose.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@Builder
public class ItemDto {
    private final Integer id;
    private final String name;
    private final String description;
    private final Float price;
}
