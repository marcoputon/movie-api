package br.com.movie.api.service;

import br.com.movie.api.model.dto.AwardInterval;
import br.com.movie.api.model.dto.AwardIntervalResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwardIntervalService {
    private final ProducerService producerService;


    public AwardIntervalResult findBiggestAndSmallestIntervals() {
        log.info("MSG=start");

        AwardInterval biggestProducer = new AwardInterval();
        AwardInterval smallestProducer = new AwardInterval();

        List<AwardInterval> biggestProducerList = new ArrayList<>();
        List<AwardInterval> smallestProducerList = new ArrayList<>();


        List<String> producersWithMultipleWins = producerService.findProducersWithMultipleAwards();
        log.debug("producersWithMultipleWins={}", producersWithMultipleWins.toString());

        if (!producersWithMultipleWins.isEmpty()) {
            var desProducer = producersWithMultipleWins.get(0);
            var intervals = findIntervals(desProducer);

            biggestProducer.set(intervals.getFirst());
            smallestProducer.set(intervals.getSecond());

            biggestProducerList.add(biggestProducer);
            smallestProducerList.add(smallestProducer);
        }

        for (String desProducer : producersWithMultipleWins) {
            var intervals = findIntervals(desProducer);
            var biggest = intervals.getFirst();
            var smallest = intervals.getSecond();

            if (biggest.isSameSize(biggestProducer) && biggest.isDifferentProducer(biggestProducer)) {
                biggestProducerList.add(biggest);
            }
            else if (biggest.isBiggerThan(biggestProducer)) {
                biggestProducer.set(biggest);
                biggestProducerList.clear();
                biggestProducerList.add(biggest);
            }

            if (smallest.isSameSize(smallestProducer) && smallest.isDifferentProducer(smallestProducer)) {
                smallestProducerList.add(smallest);
            }
            else if (smallest.isSmallerThan(smallestProducer)) {
                smallestProducer.set(smallest);
                smallestProducerList.clear();
                smallestProducerList.add(smallest);
            }
        }

        log.info("MSG=end");
        return AwardIntervalResult.builder()
                .max(biggestProducerList)
                .min(smallestProducerList)
                .build();
    }


    private Pair<AwardInterval, AwardInterval> findIntervals(String desProducer) {
        var years = producerService.findYearsByDesProducer(desProducer);
        log.debug("years={}", years);

        AwardInterval resultBiggestProducer = buildInitialAwardInterval(desProducer);
        AwardInterval resultSmallestProducer = buildInitialAwardInterval(desProducer);
        AwardInterval biggestProducer = buildInitialAwardInterval(desProducer);
        AwardInterval smallestProducer = buildInitialAwardInterval(desProducer);

        for (Integer year : years) {
            biggestProducer.nextYear(year);
            smallestProducer.nextYear(year);

            if (biggestProducer.isBiggerThan(resultBiggestProducer)) {
                resultBiggestProducer.set(biggestProducer);
            }

            if (smallestProducer.isSmallerThan(resultSmallestProducer)) {
                resultSmallestProducer.set(smallestProducer);
            }
        }

        return Pair.of(resultBiggestProducer, resultSmallestProducer);
    }


    private AwardInterval buildInitialAwardInterval(String desProducer) {
        return AwardInterval.builder()
                .producer(desProducer)
                .build();
    }
}
