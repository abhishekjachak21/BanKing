package com.bank.transaction.scheduler;

import com.bank.transaction.producer.DailyInterestProducer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailyInterestScheduler {

    private final DailyInterestProducer producer;

    @Scheduled(cron = "0 0 0 * * *")
    public void runDailyInterestJob() {

        log.info("Publishing Daily Interest Event");

        producer.publishDailyInterestJob();
    }
}