package br.com.movie.api;

import br.com.movie.api.model.Award;
import br.com.movie.api.repository.AwardRepository;
import br.com.movie.api.repository.ProducerRepository;
import br.com.movie.api.service.AwardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AwardServiceTest {

    @Autowired
    private AwardService awardService;

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private AwardRepository awardRepository;


    @BeforeEach
    void clearDatabase() {
        producerRepository.deleteAll();
        awardRepository.deleteAll();
    }


    @Test
    void shouldPopulateProducersWithWinners() {
        awardRepository.saveAndFlush(buildAward("1, 2, 3", 1996, true));
        awardRepository.saveAndFlush(buildAward("4, 6", 1996, true));
        awardRepository.saveAndFlush(buildAward("3", 1997, true));
        awardRepository.saveAndFlush(buildAward("2", 1997, false));
        awardRepository.saveAndFlush(buildAward("4 and 5", 1999, true));

        awardService.populateProducerTableWithProducersFromAward();

        var producers = producerRepository.findAll();
        assertEquals(8, producers.size());
    }


    @Test
    void shouldBreakProducersOnComma() {
        awardRepository.saveAndFlush(buildAward("1,2, 3", 1996, true));
        awardService.populateProducerTableWithProducersFromAward();
        assertForSeparators();
    }


    @Test
    void shouldBreakProducersOnAND() {
        awardRepository.saveAndFlush(buildAward("1 and 2 and 3", 1996, true));
        awardService.populateProducerTableWithProducersFromAward();
        assertForSeparators();
    }


    @Test
    void shouldBreakProducersOnCommaAndAND() {
        awardRepository.saveAndFlush(buildAward("1, 2 and 3", 1996, true));
        awardService.populateProducerTableWithProducersFromAward();
        assertForSeparators();
    }


    @Test
    void shouldBreakProducersOnCommaAndANDAtTheSameWord() {
        awardRepository.saveAndFlush(buildAward("1, 2, and 3", 1996, true));
        awardService.populateProducerTableWithProducersFromAward();
        assertForSeparators();
    }


    private void assertForSeparators() {
        var producers = producerRepository.findAll(Sort.by("desProducer"));

        assertEquals(3, producers.size());
        assertEquals("1", producers.get(0).getDesProducer());
        assertEquals("2", producers.get(1).getDesProducer());
        assertEquals("3", producers.get(2).getDesProducer());
    }


    private Award buildAward(String desProducers, int numYear, boolean isWinner) {
        return Award.builder()
                .desProducers(desProducers)
                .desStudios("Studios")
                .desTitle("Title")
                .desWinner(isWinner ? "yes" : null)
                .numYear(numYear)
                .build();
    }
}
