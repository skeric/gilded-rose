package com.miw.gildedrose.repository;

import com.miw.gildedrose.model.ItemView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

public interface ItemViewRepository extends JpaRepository<ItemView, Integer> {

    @Query(value = "SELECT COUNT(1) FROM item_view WHERE item_id = :itemId AND created_at BETWEEN :from AND :to")
    Integer findViewCount(Integer itemId, Timestamp from, Timestamp to);
}
