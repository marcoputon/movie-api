package br.com.movie.api.repository;

import br.com.movie.api.model.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProducerRepository extends JpaRepository<Producer, Long> {

    @Query(
            nativeQuery =true,
            value =
                    "SELECT \n" +
                    "    des_producer \n" +
                    "FROM ( \n" +
                    "    SELECT \n" +
                    "        des_producer, \n" +
                    "        count(*) AS total \n" +
                    "    FROM \n" +
                    "        PRODUCER p \n" +
                    "        JOIN AWARD a ON \n" +
                    "            a.id_award = p.id_award \n" +
                    "            AND a.des_winner IS NOT NULL \n" +
                    "            AND UPPER(a.des_winner) = 'YES' \n" +
                    "        GROUP BY \n" +
                    "            des_producer \n" +
                    "        ORDER BY \n" +
                    "            count(*) DESC \n" +
                    ") \n" +
                    "WHERE \n" +
                    "    total > 1 \n" +
                    "ORDER BY \n" +
                    "    des_producer")
    List<String> findProducersWithMultipleAwards();


    @Query(
            nativeQuery =true,
            value =
                    "SELECT \n" +
                    "    num_year \n" +
                    "FROM \n" +
                    "    PRODUCER p \n" +
                    "    JOIN AWARD a ON \n" +
                    "        a.id_award = p.id_award \n" +
                    "WHERE  \n" +
                    "    p.des_producer = :desProducer \n" +
                    "ORDER BY \n" +
                    "   num_year DESC")
    List<Integer> findYearsByDesProducer(String desProducer);
    
}
