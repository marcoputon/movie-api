package br.com.movie.api.repository;

import br.com.movie.api.model.ProducerInterval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProducerIntervalRepository extends JpaRepository<ProducerInterval, Long> {

    @Query(
            nativeQuery =true,
            value =
                    "SELECT \n" +
                    "    MIN(NUM_INTERVAL) \n" +
                    "FROM \n" +
                    "    PRODUCER_INTERVAL")
    Integer findMinInterval();


    @Query(
            nativeQuery =true,
            value =
                    "SELECT \n" +
                    "    MAX(NUM_INTERVAL) \n" +
                    "FROM \n" +
                    "    PRODUCER_INTERVAL")
    Integer findMaxInterval();


    @Query(
            nativeQuery =true,
            value =
                    "SELECT \n" +
                    "    * \n" +
                    "FROM \n" +
                    "    PRODUCER_INTERVAL \n" +
                    "WHERE \n" +
                    "   NUM_INTERVAL = :numInterval \n" +
                    "ORDER BY \n" +
                    "   DES_PRODUCER ASC")
    List<ProducerInterval> findByNumInterval(Integer numInterval);

}
