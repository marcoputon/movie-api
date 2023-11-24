package br.com.movie.api;

import br.com.movie.api.model.ProducerInterval;
import br.com.movie.api.repository.AwardWinningProducerRepository;
import br.com.movie.api.repository.ProducerIntervalRepository;
import br.com.movie.api.service.IntervalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntervalServiceTest {

    @Autowired
    private IntervalService intervalService;

    @Autowired
    private ProducerIntervalRepository producerIntervalRepository;

    @Autowired
    private AwardWinningProducerRepository awardWinningProducerRepository;


    @BeforeEach
    void clearDatabase() {
        producerIntervalRepository.deleteAll();
        awardWinningProducerRepository.deleteAll();
    }


    @Test
    @DisplayName("MIN[A], MAX[B]")
    void test1() {
        producerIntervalRepository.saveAndFlush(buildProducerInterval("A", 1990, 1992));
        producerIntervalRepository.saveAndFlush(buildProducerInterval("B", 1990, 1995));
        producerIntervalRepository.saveAndFlush(buildProducerInterval("C", 1991, 1994));

        var result = intervalService.findMinAndMaxIntervals();

        // MIN asserts
        assertEquals(1, result.getMin().size());
        assertEquals("A", result.getMin().get(0).getProducer());
        assertEquals(1990, result.getMin().get(0).getPreviousWin());
        assertEquals(1992, result.getMin().get(0).getFollowingWin());
        assertEquals(2, result.getMin().get(0).getInterval());

        // MAX asserts
        assertEquals(1, result.getMax().size());
        assertEquals("B", result.getMax().get(0).getProducer());
        assertEquals(1990, result.getMax().get(0).getPreviousWin());
        assertEquals(1995, result.getMax().get(0).getFollowingWin());
        assertEquals(5, result.getMax().get(0).getInterval());
    }


    @Test
    @DisplayName("MIN[A], MAX[A]")
    void test2() {
        producerIntervalRepository.saveAndFlush(buildProducerInterval("A", 1990, 1992));

        var result = intervalService.findMinAndMaxIntervals();

        // MIN asserts
        assertEquals(1, result.getMin().size());
        assertEquals("A", result.getMin().get(0).getProducer());
        assertEquals(1990, result.getMin().get(0).getPreviousWin());
        assertEquals(1992, result.getMin().get(0).getFollowingWin());
        assertEquals(2, result.getMin().get(0).getInterval());

        // MAX asserts
        assertEquals(1, result.getMax().size());
        assertEquals("A", result.getMax().get(0).getProducer());
        assertEquals(1990, result.getMax().get(0).getPreviousWin());
        assertEquals(1992, result.getMax().get(0).getFollowingWin());
        assertEquals(2, result.getMax().get(0).getInterval());
    }


    @Test
    @DisplayName("MIN[A, B], MAX[A, B]")
    void test3() {
        producerIntervalRepository.saveAndFlush(buildProducerInterval("A", 1990, 1992));
        producerIntervalRepository.saveAndFlush(buildProducerInterval("A", 1992, 1994));
        producerIntervalRepository.saveAndFlush(buildProducerInterval("B", 1990, 1995));
        producerIntervalRepository.saveAndFlush(buildProducerInterval("B", 1995, 2000));
        producerIntervalRepository.saveAndFlush(buildProducerInterval("C", 1991, 1994));

        var result = intervalService.findMinAndMaxIntervals();

        // MIN asserts
        assertEquals(2, result.getMin().size());

        assertEquals("A", result.getMin().get(0).getProducer());
        assertEquals(1990, result.getMin().get(0).getPreviousWin());
        assertEquals(1992, result.getMin().get(0).getFollowingWin());
        assertEquals(2, result.getMin().get(0).getInterval());

        assertEquals("A", result.getMin().get(1).getProducer());
        assertEquals(1992, result.getMin().get(1).getPreviousWin());
        assertEquals(1994, result.getMin().get(1).getFollowingWin());
        assertEquals(2, result.getMin().get(1).getInterval());

        // MAX asserts
        assertEquals(2, result.getMax().size());

        assertEquals("B", result.getMax().get(0).getProducer());
        assertEquals(1990, result.getMax().get(0).getPreviousWin());
        assertEquals(1995, result.getMax().get(0).getFollowingWin());
        assertEquals(5, result.getMax().get(0).getInterval());

        assertEquals("B", result.getMax().get(1).getProducer());
        assertEquals(1995, result.getMax().get(1).getPreviousWin());
        assertEquals(2000, result.getMax().get(1).getFollowingWin());
        assertEquals(5, result.getMax().get(1).getInterval());
    }


    @Test
    @DisplayName("MIN[], MAX[] - empty interval table")
    void test4() {
        var result = intervalService.findMinAndMaxIntervals();

        // MIN asserts
        assertEquals(0, result.getMin().size());

        // MAX asserts
        assertEquals(0, result.getMax().size());
    }


    private ProducerInterval buildProducerInterval(String desProducer, int numYearStart, int numYearEnd) {
        return ProducerInterval.builder()
                .desProducer(desProducer)
                .numYearStart(numYearStart)
                .numYearEnd(numYearEnd)
                .numInterval(numYearEnd - numYearStart)
                .build();
    }
}
