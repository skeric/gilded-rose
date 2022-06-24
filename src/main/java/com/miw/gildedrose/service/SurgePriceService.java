package com.miw.gildedrose.service;

import com.miw.gildedrose.repository.ItemViewRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class SurgePriceService {
    private final int duration;
    private final int viewCountTriggerAdjustment;
    private final int adjustmentPercent;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private final ItemViewRepository itemViewRepository;

    public SurgePriceService(@Value("${surgePricing.durationInMinutes}") int duration,
                             @Value("${surgePricing.viewCountsTriggerPriceIncrease}") int viewCountTriggerAdjustment,
                             @Value("${surgePricing.percentageIncrease}") int adjustmentPercent,
                             ItemViewRepository itemViewRepository) {
        this.duration = duration;
        this.viewCountTriggerAdjustment = viewCountTriggerAdjustment;
        this.adjustmentPercent = adjustmentPercent;
        this.itemViewRepository = itemViewRepository;
    }

    public Float adjustPrice(Integer itemId, Float basePrice) {
        Integer viewCount = getViewCount(itemId);
        return adjustPriceIfNecessary(viewCount ,basePrice);
    }

    private Integer getViewCount(Integer itemId) {
        Instant current = Instant.now();
        Instant fromTime = current.minus(duration, ChronoUnit.MINUTES);
        return itemViewRepository.findViewCount(itemId, Timestamp.from(fromTime), Timestamp.from(current));
    }

    private Float adjustPriceIfNecessary(Integer viewCount, Float basePrice) {
        if (viewCount >= viewCountTriggerAdjustment) {
            float adjustment = basePrice * adjustmentPercent / 100;
            float newPrice = adjustment + basePrice;
            return Float.parseFloat(df.format(newPrice));
        }

        return basePrice;
    }
}
