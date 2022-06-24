package com.miw.gildedrose.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
@EqualsAndHashCode
public class OrderDto {
    private final String orderNumber;
    private final String name;
    private final Float price;
    private final Integer quantity;
    private final String errorMessage;
}
