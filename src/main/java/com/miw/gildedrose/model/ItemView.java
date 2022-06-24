package com.miw.gildedrose.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "item_view")
@EqualsAndHashCode
public class ItemView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "item_id", nullable = false)
    private Integer itemId;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    public ItemView(Integer itemId) {
        this.itemId = itemId;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }
}
