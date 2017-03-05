package com.aeon.metrics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Service;

/**
 * Created by roshane on 3/4/17.
 */
@Service
public class ApiHitCounter {

    private final CounterService counterService;

    @Autowired
    public ApiHitCounter(CounterService counterService) {
        this.counterService = counterService;
    }

    public void incrementApiHit(String endpointId){
        counterService.increment(endpointId);
    }
}
