package com.miw.gildedrose.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@RequiredArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class ItemViewDto {
    private final Integer id;
    private final Integer itemId;
    private final Timestamp createdAt;
}
