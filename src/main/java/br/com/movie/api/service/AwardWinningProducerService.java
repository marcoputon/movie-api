package br.com.movie.api.service;

import br.com.movie.api.model.Award;
import br.com.movie.api.model.AwardWinningProducer;
import br.com.movie.api.repository.AwardRepository;
import br.com.movie.api.repository.AwardWinningProducerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwardWinningProducerService {
    private final AwardRepository awardRepository;
    private final AwardWinningProducerRepository awardWinningProducerRepository;

    private static final String SEPARATOR_AND = " and ";
    private static final String SEPARATOR_COMMA = "\\s*,\\s*";
    private static final int INITIAL_PAGE = 0;
    private static final int PAGE_SIZE = 500;


    public void save(AwardWinningProducer awardWinningProducer) {
        awardWinningProducerRepository.saveAndFlush(awardWinningProducer);
    }


    public List<String> findDistinctAwardWinningProducerByPage(Pageable pageable) {
        return awardWinningProducerRepository.findDistinctAwardWinningProducerByPage(pageable);
    }


    public List<AwardWinningProducer> findAwardWinningProducersByNamProducerByPage(String desProducer, Pageable pageable) {
        return awardWinningProducerRepository.findAwardWinningProducersByNamProducerByPage(desProducer, pageable);
    }


    public void populateAwardWinningProducer() {
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
                desProducer -> save(
                        buildAwardWinningProducer(
                                desProducer,
                                award.getNumYear(),
                                award.getIdAward()
                        )
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


    private AwardWinningProducer buildAwardWinningProducer(String desProducer, Integer numYear, Long idAward) {
        return AwardWinningProducer.builder()
                .desProducer(desProducer)
                .numYear(numYear)
                .idAward(idAward)
                .build();
    }


    private List<String> splitBySeparator(String value, String regex) {
        return new ArrayList<>(Arrays.asList(value.split(regex)));
    }
}
