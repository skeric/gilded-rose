package com.miw.gildedrose.repository;

import com.miw.gildedrose.model.ItemStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;

public interface ItemStockRepository extends JpaRepository<ItemStock, Integer> {

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT s FROM item_stock s WHERE s.itemId = :itemId")
    ItemStock findAndLock(Integer itemId);
}
