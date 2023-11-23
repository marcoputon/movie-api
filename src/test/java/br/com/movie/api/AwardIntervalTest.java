package br.com.movie.api;


import br.com.movie.api.model.Award;
import br.com.movie.api.model.ProducerInterval;
import br.com.movie.api.repository.AwardRepository;
import br.com.movie.api.repository.ProducerIntervalRepository;
import org.junit.jupiter.api.BeforeEach;
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
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;


    @BeforeEach
    void clearDatabase() {
        producerIntervalRepository.deleteAll();
        awardRepository.deleteAll();
    }


    @Test
    void shouldReturnMinAndMax() {
        awardRepository.saveAndFlush(buildAward("1, 2, 3", 1996, true));
        awardRepository.saveAndFlush(buildAward("4, 6", 1996, true));
        awardRepository.saveAndFlush(buildAward("3", 1997, true));
        awardRepository.saveAndFlush(buildAward("2", 1997, false));
        awardRepository.saveAndFlush(buildAward("4 and 5", 1999, true));

        saveProducerInterval("2", 1996, 1997, 1);
        saveProducerInterval("3", 1996, 1997, 1);
        saveProducerInterval("4", 1996, 1999, 3);

        var firstCaseResponse =
                "{" +
                    "\"min\":[" +
                        "{" +
                            "\"producer\":\"2\"," +
                            "\"interval\":1," +
                            "\"previousWin\":1996," +
                            "\"followingWin\":1997" +
                        "}," +
                        "{" +
                            "\"producer\":\"3\"," +
                            "\"interval\":1," +
                            "\"previousWin\":1996," +
                            "\"followingWin\":1997" +
                        "}" +
                    "]," +
                    "\"max\":[" +
                        "{" +
                            "\"producer\":\"4\"," +
                            "\"interval\":3," +
                            "\"previousWin\":1996," +
                            "\"followingWin\":1999}" +
                    "]" +
                "}";

        var response = this.restTemplate.getForObject(
                "http://localhost:" + port + "/api/award-intervals",
                String.class
        );
        assertThat(response).isEqualTo(firstCaseResponse);
    }


    @Test
    void shouldReturnSameMinAndMaxWhenItsTheOnlyOneWithMoreThanOneWinning() {
        awardRepository.saveAndFlush(buildAward("1, 2, 3", 1996, true));
        awardRepository.saveAndFlush(buildAward("4, 6", 1996, true));
        awardRepository.saveAndFlush(buildAward("3", 1997, true));

        saveProducerInterval("3", 1996, 1997, 1);

        var firstCaseResponse =
                "{" +
                    "\"min\":[" +
                        "{" +
                            "\"producer\":\"3\"," +
                            "\"interval\":1," +
                            "\"previousWin\":1996," +
                            "\"followingWin\":1997" +
                        "}" +
                    "]," +
                    "\"max\":[" +
                        "{" +
                            "\"producer\":\"3\"," +
                            "\"interval\":1," +
                            "\"previousWin\":1996," +
                            "\"followingWin\":1997}" +
                    "]" +
                "}";

        var response = this.restTemplate.getForObject(
                "http://localhost:" + port + "/api/award-intervals",
                String.class
        );
        assertThat(response).isEqualTo(firstCaseResponse);
    }


    @Test
    void shouldReturnMinAndMaxForMultipleResults() {
        awardRepository.saveAndFlush(buildAward("1, 2, 3, 4", 1996, true));
        awardRepository.saveAndFlush(buildAward("2", 1997, true));
        awardRepository.saveAndFlush(buildAward("4, 6", 1998, true));
        awardRepository.saveAndFlush(buildAward("3", 1998, true));

        saveProducerInterval("2", 1996, 1997, 1);
        saveProducerInterval("3", 1996, 1998, 2);
        saveProducerInterval("4", 1996, 1998, 2);


        var firstCaseResponse =
                "{" +
                    "\"min\":[" +
                        "{" +
                            "\"producer\":\"2\"," +
                            "\"interval\":1," +
                            "\"previousWin\":1996," +
                            "\"followingWin\":1997" +
                        "}" +
                    "]," +
                    "\"max\":[" +
                        "{" +
                            "\"producer\":\"3\"," +
                            "\"interval\":2," +
                            "\"previousWin\":1996," +
                            "\"followingWin\":1998" +
                        "}," +
                        "{" +
                            "\"producer\":\"4\"," +
                            "\"interval\":2," +
                            "\"previousWin\":1996," +
                            "\"followingWin\":1998" +
                        "}" +
                    "]" +
                "}";

        var response = this.restTemplate.getForObject(
                "http://localhost:" + port + "/api/award-intervals",
                String.class
        );
        assertThat(response).isEqualTo(firstCaseResponse);
    }


    @Test
    void shouldReturnEmptyListWhenThereIsNoOneWithMoreThenTwoWinnings() {
        var firstCaseResponse =
                "{" +
                    "\"min\":[]," +
                    "\"max\":[]" +
                "}";

        var response = this.restTemplate.getForObject(
                "http://localhost:" + port + "/api/award-intervals",
                String.class
        );
        assertThat(response).isEqualTo(firstCaseResponse);
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

    private void saveProducerInterval(String desProducer, int numYearStart, int numYearEnd, int numInterval) {
        producerIntervalRepository.saveAndFlush(
                ProducerInterval.builder()
                        .desProducer(desProducer)
                        .numYearStart(numYearStart)
                        .numYearEnd(numYearEnd)
                        .numInterval(numInterval)
                        .build()
        );
    }
}
