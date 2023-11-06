package br.com.movie.api.service;


import br.com.movie.api.model.Award;
import br.com.movie.api.model.Producer;
import br.com.movie.api.repository.AwardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwardService {
    private final AwardRepository awardRepository;
    private final ProducerService producerService;

    private static final int INITIAL_PAGE = 0;
    private static final int PAGE_SIZE = 500;
    private static final String SEPARATOR_AND = " and ";
    private static final String SEPARATOR_COMMA = "\\s*,\\s*";


    @EventListener(ApplicationReadyEvent.class)
    public void populateProducerTableWithProducersFromAward() {
        log.info("MSG=Start");
        Pageable pageRequest = PageRequest.of(INITIAL_PAGE, PAGE_SIZE);

        var awardList = awardRepository.findWinnerProducersByPage(pageRequest);
        while (!awardList.isEmpty()) {
            awardList.forEach(this::insertProducerInDataBase);

            pageRequest = pageRequest.next();
            awardList = awardRepository.findWinnerProducersByPage(pageRequest);
        }
        log.info("MSG=Finish");
    }


    private void insertProducerInDataBase(Award award) {
        var awardProducers = convertAwardToListOfDesProducer(award);

        awardProducers.forEach(
                desProducer -> producerService.saveProducer(
                        buildProducerFromAward(award.getIdAward(), desProducer)
                )
        );
    }


    private List<String> convertAwardToListOfDesProducer(Award award) {
        List<String> firstSplited = splitBySeparator(award.getDesProducers().trim(), SEPARATOR_AND);

        List<String> awardProducers = new ArrayList<>();
        firstSplited.forEach(
                item -> awardProducers.addAll(splitBySeparator(item, SEPARATOR_COMMA))
        );
        awardProducers.replaceAll(String::trim);

        return awardProducers;
    }


    private List<String> splitBySeparator(String value, String regex) {
        return new ArrayList<>(Arrays.asList(value.split(regex)));
    }


    private Producer buildProducerFromAward(Long idAward, String desProducer) {
        return Producer.builder()
                .desProducer(desProducer)
                .idAward(idAward)
                .build();
    }
}
