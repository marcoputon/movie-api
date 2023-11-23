package br.com.movie.api;

import br.com.movie.api.model.Award;
import br.com.movie.api.repository.AwardRepository;
import br.com.movie.api.repository.AwardWinningProducerRepository;
import br.com.movie.api.repository.ProducerIntervalRepository;
import br.com.movie.api.service.AwardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AwardServiceTest {

    @Autowired
    private AwardService awardService;

    @Autowired
    private AwardRepository awardRepository;

    @Autowired
    private ProducerIntervalRepository producerIntervalRepository;

    @Autowired
    private AwardWinningProducerRepository awardWinningProducerRepository;


    @BeforeEach
    void clearDatabase() {
        producerIntervalRepository.deleteAll();
        awardWinningProducerRepository.deleteAll();
        awardRepository.deleteAll();
    }


    @Test
    void shouldPopulateProducersWithWinners() {
        awardRepository.saveAndFlush(buildAward("1, 2, 3", 1996, true));
        awardRepository.saveAndFlush(buildAward("4, 6", 1996, true));
        awardRepository.saveAndFlush(buildAward("3", 1997, true));
        awardRepository.saveAndFlush(buildAward("2", 1997, false));
        awardRepository.saveAndFlush(buildAward("4 and 5", 1999, true));

        awardService.populateTables();

        var producers = producerIntervalRepository.findAll();
        assertEquals(2, producers.size());
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
