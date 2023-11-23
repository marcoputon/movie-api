package br.com.movie.api.service;

import br.com.movie.api.model.ProducerInterval;
import br.com.movie.api.model.dto.AwardInterval;
import br.com.movie.api.model.dto.AwardIntervalResult;
import br.com.movie.api.repository.ProducerIntervalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntervalService {
    private final ProducerIntervalRepository producerIntervalRepository;

    public AwardIntervalResult findMinAndMaxIntervals() {
        var min = producerIntervalRepository.findMinInterval();
        var max = producerIntervalRepository.findMaxInterval();

        if (isNull(min) || isNull(max))
            return buildEmptyResult();

        var minList = producerIntervalRepository.findByNumInterval(min);
        var maxList = producerIntervalRepository.findByNumInterval(max);

        return AwardIntervalResult.builder()
                .min(convertProducerIntervalListToAwardInterval(minList))
                .max(convertProducerIntervalListToAwardInterval(maxList))
                .build();
    }


    private List<AwardInterval> convertProducerIntervalListToAwardInterval(List<ProducerInterval> producerIntervalList) {
        List<AwardInterval> list = new ArrayList<>();

        producerIntervalList.forEach(
                producerInterval -> list.add(buildAwardInterval(producerInterval))
        );
        return list;
    }


    private AwardInterval buildAwardInterval(ProducerInterval producerInterval) {
        return AwardInterval.builder()
                .producer(producerInterval.getDesProducer())
                .previousWin(producerInterval.getNumYearStart())
                .followingWin(producerInterval.getNumYearEnd())
                .interval(producerInterval.getNumInterval())
                .build();
    }


    private AwardIntervalResult buildEmptyResult() {
        return AwardIntervalResult.builder()
                .min(new ArrayList<>())
                .max(new ArrayList<>())
                .build();
    }
}
