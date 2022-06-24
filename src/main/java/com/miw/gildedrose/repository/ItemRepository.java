package com.miw.gildedrose.repository;

import com.miw.gildedrose.model.Item;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ItemRepository extends PagingAndSortingRepository<Item, Integer> {
    Item findByName(String name);
}
