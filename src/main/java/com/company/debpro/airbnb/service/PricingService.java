package com.company.debpro.airbnb.service;

import com.company.debpro.airbnb.entity.Inventory;
import com.company.debpro.airbnb.strategy.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

//******************************************Decorator pattern is used here******************************
@Service
public class PricingService {
    public BigDecimal calculateDynamicPricing(Inventory inventory){
        PricingStrategy pricingStrategy = new BasePricingStrategy();

        //Apply the additional strategies
        pricingStrategy = new SurgePricingStrategy(pricingStrategy);
        pricingStrategy = new OccupancyPricingStrategy(pricingStrategy);
        pricingStrategy = new UrgencyPricingStrategy(pricingStrategy);

        return pricingStrategy.calculatePrice(inventory);
    }
}
