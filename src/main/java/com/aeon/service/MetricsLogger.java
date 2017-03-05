package com.aeon.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.SystemPublicMetrics;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by roshane on 3/4/17.
 */
@Component
public class MetricsLogger {

    @Autowired
    private SystemPublicMetrics publicMetrics;

    private static final Logger logger= LoggerFactory.getLogger(MetricsLogger.class);

//    @Scheduled(fixedDelay = 10000L)
    public void logCounterServices(){
        publicMetrics.metrics().forEach((metric -> {
            logger.info("Reporting metrics {} : {} ",metric.getName(),metric.getValue());
        }));
    }
}
