package br.com.movie.api.service;

import br.com.movie.api.model.AwardWinningProducer;
import br.com.movie.api.model.ProducerInterval;
import br.com.movie.api.repository.ProducerIntervalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerIntervalService {
    private final ProducerIntervalRepository producerIntervalRepository;
    private final AwardWinningProducerService awardWinningProducerService;

    private static final int INITIAL_PAGE = 0;
    private static final int PAGE_SIZE = 500;


    public void save(ProducerInterval producerInterval) {
        producerIntervalRepository.saveAndFlush(producerInterval);
    }


    public void populateProducerIntervalTable() {
        log.info("MSG=Start");
        Pageable pageRequest = PageRequest.of(INITIAL_PAGE, PAGE_SIZE);

        var desProducerList = awardWinningProducerService.findDistinctAwardWinningProducerByPage(pageRequest);
        while (!desProducerList.isEmpty()) {
            desProducerList.forEach(this::createAwardWinningProducersForProducer);

            pageRequest = pageRequest.next();
            desProducerList = awardWinningProducerService.findDistinctAwardWinningProducerByPage(pageRequest);
        }
        log.info("MSG=Finish");
    }


    private void createAwardWinningProducersForProducer(String desProducer) {
        Pageable pageRequest = PageRequest.of(INITIAL_PAGE, PAGE_SIZE);

        var awardWinningProducerList = awardWinningProducerService.findAwardWinningProducersByNamProducerByPage(desProducer, pageRequest);
        if (awardWinningProducerList.size() < 2) return;

        AwardWinningProducer start;
        AwardWinningProducer end = awardWinningProducerList.get(0);
        var numInterval = 0;

        while (!awardWinningProducerList.isEmpty()) {
            for (AwardWinningProducer awardWinningProducer : awardWinningProducerList) {
                start = end.getCopy();
                end = awardWinningProducer.getCopy();
                numInterval = end.getNumYear() - start.getNumYear();

                if (numInterval > 0) {
                    this.save(buildProducerInterval(start, end, numInterval));
                }

            }

            pageRequest = pageRequest.next();
            awardWinningProducerList = awardWinningProducerService.findAwardWinningProducersByNamProducerByPage(desProducer, pageRequest);
        }
    }





    private ProducerInterval buildProducerInterval(AwardWinningProducer start, AwardWinningProducer end, int numInterval) {
        return ProducerInterval.builder()
                .desProducer(end.getDesProducer())
                .numYearStart(start.getNumYear())
                .numYearEnd(end.getNumYear())
                .numInterval(numInterval)
                .build();
    }
}
