package br.com.movie.api;


import br.com.movie.api.controller.AwardIntervalController;
import br.com.movie.api.model.Award;
import br.com.movie.api.model.Producer;
import br.com.movie.api.repository.AwardRepository;
import br.com.movie.api.repository.ProducerRepository;
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
    private ProducerRepository producerRepository;

    @Autowired
    private AwardRepository awardRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;


    @BeforeEach
    void clearDatabase() {
        producerRepository.deleteAll();
        awardRepository.deleteAll();
    }


    @Test
    void shouldReturnMinAndMax() {
        var award = awardRepository.saveAndFlush(buildAward("1, 2, 3", 1996, true));
        saveProducer("1", award.getIdAward());
        saveProducer("2", award.getIdAward());
        saveProducer("3", award.getIdAward());

        award = awardRepository.saveAndFlush(buildAward("4, 6", 1996, true));
        saveProducer("4", award.getIdAward());
        saveProducer("6", award.getIdAward());

        award = awardRepository.saveAndFlush(buildAward("3", 1997, true));
        saveProducer("3", award.getIdAward());

        award = awardRepository.saveAndFlush(buildAward("2", 1997, false));
        saveProducer("2", award.getIdAward());

        award = awardRepository.saveAndFlush(buildAward("4 and 5", 1999, true));
        saveProducer("4", award.getIdAward());
        saveProducer("5", award.getIdAward());

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
        var award = awardRepository.saveAndFlush(buildAward("1, 2, 3", 1996, true));
        saveProducer("1", award.getIdAward());
        saveProducer("2", award.getIdAward());
        saveProducer("3", award.getIdAward());

        award = awardRepository.saveAndFlush(buildAward("4, 6", 1996, true));
        saveProducer("4", award.getIdAward());
        saveProducer("6", award.getIdAward());

        award = awardRepository.saveAndFlush(buildAward("3", 1997, true));
        saveProducer("3", award.getIdAward());

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
        var award = awardRepository.saveAndFlush(buildAward("1, 2, 3, 4", 1996, true));
        saveProducer("1", award.getIdAward());
        saveProducer("2", award.getIdAward());
        saveProducer("3", award.getIdAward());
        saveProducer("4", award.getIdAward());

        award = awardRepository.saveAndFlush(buildAward("2", 1997, true));
        saveProducer("2", award.getIdAward());

        award = awardRepository.saveAndFlush(buildAward("4, 6", 1998, true));
        saveProducer("4", award.getIdAward());
        saveProducer("6", award.getIdAward());

        award = awardRepository.saveAndFlush(buildAward("3", 1998, true));
        saveProducer("3", award.getIdAward());

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

    private void saveProducer(String desProducer, long idAward) {
        producerRepository.saveAndFlush(
                Producer.builder()
                        .desProducer(desProducer)
                        .idAward(idAward)
                        .build()
        );
    }
}
