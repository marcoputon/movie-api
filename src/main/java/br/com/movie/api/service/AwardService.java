package br.com.movie.api.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwardService {
    private final AwardWinningProducerService awardWinningProducerService;
    private final ProducerIntervalService producerIntervalService;


    @EventListener(ApplicationReadyEvent.class)
    public void populateTables() {
        awardWinningProducerService.populateAwardWinningProducer();
        producerIntervalService.populateProducerIntervalTable();
    }
}
