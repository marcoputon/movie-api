package br.com.movie.api;


import br.com.movie.api.model.Award;
import br.com.movie.api.repository.AwardRepository;
import br.com.movie.api.repository.AwardWinningProducerRepository;
import br.com.movie.api.repository.ProducerIntervalRepository;
import br.com.movie.api.service.AwardWinningProducerService;
import br.com.movie.api.service.ProducerIntervalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AwardIntervalTest {

    @Autowired
    private AwardRepository awardRepository;

    @Autowired
    private ProducerIntervalRepository producerIntervalRepository;

    @Autowired
    private AwardWinningProducerService awardWinningProducerService;

    @Autowired
    private ProducerIntervalService producerIntervalService;

    @Autowired
    private AwardWinningProducerRepository awardWinningProducerRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;


    @BeforeEach
    void clearDatabase() {
        awardWinningProducerRepository.deleteAll();
        producerIntervalRepository.deleteAll();
        awardRepository.deleteAll();
    }


    @Test
    @DisplayName("MIN[A], MAX[B]")
    void test1() {
        awardRepository.saveAndFlush(buildAward("A, B and X", 1996));
        awardRepository.saveAndFlush(buildAward("A", 1997));
        awardRepository.saveAndFlush(buildAward("B", 1999));

        awardWinningProducerService.populateAwardWinningProducer();
        producerIntervalService.populateProducerIntervalTable();

        var expectedResponse = buildStringIntervalResponse(
                buildStringInterval("A", "1", "1996", "1997"),
                buildStringInterval("B", "3", "1996", "1999")
        );

        var response = this.restTemplate.getForObject(
                "http://localhost:" + port + "/api/award-intervals",
                String.class
        );
        assertThat(response).isEqualTo(expectedResponse);
    }


    @Test
    @DisplayName("MIN[A], MAX[A]")
    void test2() {
        awardRepository.saveAndFlush(buildAward("A, B and X", 1996));
        awardRepository.saveAndFlush(buildAward("A", 1997));

        awardWinningProducerService.populateAwardWinningProducer();
        producerIntervalService.populateProducerIntervalTable();

        var expectedResponse = buildStringIntervalResponse(
                buildStringInterval("A", "1", "1996", "1997"),
                buildStringInterval("A", "1", "1996", "1997")
        );

        var response = this.restTemplate.getForObject(
                "http://localhost:" + port + "/api/award-intervals",
                String.class
        );
        assertThat(response).isEqualTo(expectedResponse);
    }


    @Test
    @DisplayName("MIN[A, B], MAX[A, B]")
    void test3() {
        awardRepository.saveAndFlush(buildAward("A, B and X", 1996));
        awardRepository.saveAndFlush(buildAward("A", 1997));
        awardRepository.saveAndFlush(buildAward("B", 1997));

        awardWinningProducerService.populateAwardWinningProducer();
        producerIntervalService.populateProducerIntervalTable();

        var expectedResponse = buildStringIntervalResponse(
                buildStringInterval("A", "1", "1996", "1997") + "," + buildStringInterval("B", "1", "1996", "1997"),
                buildStringInterval("A", "1", "1996", "1997") + "," + buildStringInterval("B", "1", "1996", "1997")
        );

        var response = this.restTemplate.getForObject(
                "http://localhost:" + port + "/api/award-intervals",
                String.class
        );
        assertThat(response).isEqualTo(expectedResponse);
    }


    @Test
    @DisplayName("MIN[A, B], MAX[C]")
    void test4() {
        awardRepository.saveAndFlush(buildAward("A, B and C", 1996));
        awardRepository.saveAndFlush(buildAward("A", 1997));
        awardRepository.saveAndFlush(buildAward("B", 1997));
        awardRepository.saveAndFlush(buildAward("C", 1999));

        awardWinningProducerService.populateAwardWinningProducer();
        producerIntervalService.populateProducerIntervalTable();

        var expectedResponse = buildStringIntervalResponse(
                buildStringInterval("A", "1", "1996", "1997") + "," + buildStringInterval("B", "1", "1996", "1997"),
                buildStringInterval("C", "3", "1996", "1999")
        );

        var response = this.restTemplate.getForObject(
                "http://localhost:" + port + "/api/award-intervals",
                String.class
        );
        assertThat(response).isEqualTo(expectedResponse);
    }


    @Test
    @DisplayName("MIN[A], MAX[B, C]")
    void test5() {
        awardRepository.saveAndFlush(buildAward("A, B and C", 1996));
        awardRepository.saveAndFlush(buildAward("A", 1997));
        awardRepository.saveAndFlush(buildAward("B", 1999));
        awardRepository.saveAndFlush(buildAward("C", 1999));

        awardWinningProducerService.populateAwardWinningProducer();
        producerIntervalService.populateProducerIntervalTable();

        var expectedResponse = buildStringIntervalResponse(
                buildStringInterval("A", "1", "1996", "1997"),
                buildStringInterval("B", "3", "1996", "1999") + "," + buildStringInterval("C", "3", "1996", "1999")
        );

        var response = this.restTemplate.getForObject(
                "http://localhost:" + port + "/api/award-intervals",
                String.class
        );
        assertThat(response).isEqualTo(expectedResponse);
    }


    @Test
    @DisplayName("MIN[A, B], MAX[C, D]")
    void test6() {
        awardRepository.saveAndFlush(buildAward("A, B and C", 1996));
        awardRepository.saveAndFlush(buildAward("A, B", 1997));
        awardRepository.saveAndFlush(buildAward("C, D", 1999));
        awardRepository.saveAndFlush(buildAward("D", 2002));

        awardWinningProducerService.populateAwardWinningProducer();
        producerIntervalService.populateProducerIntervalTable();

        var expectedResponse = buildStringIntervalResponse(
                buildStringInterval("A", "1", "1996", "1997") + "," + buildStringInterval("B", "1", "1996", "1997"),
                buildStringInterval("C", "3", "1996", "1999") + "," + buildStringInterval("D", "3", "1999", "2002")
        );

        var response = this.restTemplate.getForObject(
                "http://localhost:" + port + "/api/award-intervals",
                String.class
        );
        assertThat(response).isEqualTo(expectedResponse);
    }


    @Test
    @DisplayName("MIN[], MAX[]")
    void test7() {
        var expectedResponse =
                buildStringIntervalResponse("", "");

        var response = this.restTemplate.getForObject(
                "http://localhost:" + port + "/api/award-intervals",
                String.class
        );
        assertThat(response).isEqualTo(expectedResponse);
    }


    @Test
    @DisplayName("MIN[A, A], MAX[B]")
    void test8() {
        awardRepository.saveAndFlush(buildAward("A, B and C", 1996));
        awardRepository.saveAndFlush(buildAward("A", 1997));
        awardRepository.saveAndFlush(buildAward("A", 1998));
        awardRepository.saveAndFlush(buildAward("B", 1999));

        awardWinningProducerService.populateAwardWinningProducer();
        producerIntervalService.populateProducerIntervalTable();

        var expectedResponse = buildStringIntervalResponse(
                buildStringInterval("A", "1", "1996", "1997") + "," + buildStringInterval("A", "1", "1997", "1998"),
                buildStringInterval("B", "3", "1996", "1999")
        );

        var response = this.restTemplate.getForObject(
                "http://localhost:" + port + "/api/award-intervals",
                String.class
        );
        assertThat(response).isEqualTo(expectedResponse);
    }


    @Test
    @DisplayName("MIN[A, A], MAX[A, A]")
    void test9() {
        awardRepository.saveAndFlush(buildAward("A, B", 1996));
        awardRepository.saveAndFlush(buildAward("A", 1997));
        awardRepository.saveAndFlush(buildAward("A", 1998));
        awardRepository.saveAndFlush(buildAward("A", 2004));
        awardRepository.saveAndFlush(buildAward("B", 1999));
        awardRepository.saveAndFlush(buildAward("A", 2010));

        awardWinningProducerService.populateAwardWinningProducer();
        producerIntervalService.populateProducerIntervalTable();

        var expectedResponse = buildStringIntervalResponse(
                buildStringInterval("A", "1", "1996", "1997") + "," + buildStringInterval("A", "1", "1997", "1998"),
                buildStringInterval("A", "6", "1998", "2004") + "," + buildStringInterval("A", "6", "2004", "2010")
        );

        var response = this.restTemplate.getForObject(
                "http://localhost:" + port + "/api/award-intervals",
                String.class
        );
        assertThat(response).isEqualTo(expectedResponse);
    }


    @Test
    @DisplayName("MIN[A, A], MAX[B, B]")
    void test10() {
        awardRepository.saveAndFlush(buildAward("A, B", 1996));
        awardRepository.saveAndFlush(buildAward("A", 1997));
        awardRepository.saveAndFlush(buildAward("A", 1998));
        awardRepository.saveAndFlush(buildAward("B", 1999));
        awardRepository.saveAndFlush(buildAward("B", 2002));

        awardWinningProducerService.populateAwardWinningProducer();
        producerIntervalService.populateProducerIntervalTable();

        var expectedResponse = buildStringIntervalResponse(
                buildStringInterval("A", "1", "1996", "1997") + "," + buildStringInterval("A", "1", "1997", "1998"),
                buildStringInterval("B", "3", "1996", "1999") + "," + buildStringInterval("B", "3", "1999", "2002")
        );

        var response = this.restTemplate.getForObject(
                "http://localhost:" + port + "/api/award-intervals",
                String.class
        );
        assertThat(response).isEqualTo(expectedResponse);
    }


    @Test
    @DisplayName("MIN[A], MAX[B, B]")
    void test11() {
        awardRepository.saveAndFlush(buildAward("A, B and C", 1996));
        awardRepository.saveAndFlush(buildAward("A", 1997));
        awardRepository.saveAndFlush(buildAward("B", 1998));
        awardRepository.saveAndFlush(buildAward("B", 2000));

        awardWinningProducerService.populateAwardWinningProducer();
        producerIntervalService.populateProducerIntervalTable();

        var expectedResponse = buildStringIntervalResponse(
                buildStringInterval("A", "1", "1996", "1997"),
                buildStringInterval("B", "2", "1996", "1998") + "," + buildStringInterval("B", "2", "1998", "2000")
                );

        var response = this.restTemplate.getForObject(
                "http://localhost:" + port + "/api/award-intervals",
                String.class
        );
        assertThat(response).isEqualTo(expectedResponse);
    }


    private Award buildAward(String desProducers, int numYear) {
        return Award.builder()
                .desProducers(desProducers)
                .desStudios("Studios")
                .desTitle("Title")
                .desWinner("yes")
                .numYear(numYear)
                .build();
    }


    private String buildStringInterval(String producer, String interval, String previous, String following) {
        return "{" +
                    "\"producer\":\"" + producer + "\"," +
                    "\"interval\":" + interval + "," +
                    "\"previousWin\":" + previous + "," +
                    "\"followingWin\":" + following +
                "}";
    }


    private String buildStringStart() {
        return "{\"min\":[";
    }


    private String buildStringmiddle() {
        return "],\"max\":[";
    }


    private String buildStringEnd() {
        return "]}";
    }


    private String buildStringIntervalResponse(String min, String max) {
        return
                buildStringStart() +
                min +
                buildStringmiddle() +
                max +
                buildStringEnd();
    }
}
