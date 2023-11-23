package br.com.movie.api.repository;

import br.com.movie.api.model.AwardWinningProducer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AwardWinningProducerRepository extends JpaRepository<AwardWinningProducer, Long> {

    @Query(
            nativeQuery =true,
            value =
                    "SELECT \n" +
                    "   DISTINCT DES_PRODUCER \n" +
                    "FROM \n" +
                    "   AWARD_WINNING_PRODUCER \n" +
                    "ORDER BY \n" +
                    "   DES_PRODUCER")
    List<String> findDistinctAwardWinningProducerByPage(Pageable pageable);


    @Query(
            nativeQuery =true,
            value =
                    "SELECT \n" +
                    "   * \n" +
                    "FROM \n" +
                    "   AWARD_WINNING_PRODUCER \n" +
                    "WHERE \n" +
                    "   DES_PRODUCER = :desProducer \n" +
                    "ORDER BY \n" +
                    "   NUM_YEAR ASC")
    List<AwardWinningProducer> findAwardWinningProducersByNamProducerByPage(String desProducer, Pageable pageable);

}
