package com.company.debpro.airbnb.strategy;

import com.company.debpro.airbnb.entity.Inventory;

import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal calculatePrice(Inventory inventory);
}
