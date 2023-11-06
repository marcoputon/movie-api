package br.com.movie.api.repository;

import br.com.movie.api.model.Award;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface AwardRepository extends JpaRepository<Award, Long> {

    @Query(
            nativeQuery =true,
            value =
                    "SELECT \n" +
                    "   * \n" +
                    "FROM \n" +
                    "   AWARD \n" +
                    "WHERE \n" +
                    "   des_winner IS NOT NULL AND \n" +
                    "   UPPER(des_winner) = 'YES' \n" +
                    "ORDER BY \n" +
                    "   ID_AWARD")
    List<Award> findWinnerProducersByPage(Pageable pageable);

}
