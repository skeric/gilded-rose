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

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "item_stock")
@EqualsAndHashCode
public class ItemStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "item_id", nullable = false)
    private Integer itemId;

    @Column(name = "quantity_for_sell", nullable = false)
    private Integer quantityForSell;

}
