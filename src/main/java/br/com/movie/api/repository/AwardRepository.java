package br.com.movie.api.repository;

import br.com.movie.api.model.Award;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AwardRepository extends JpaRepository<Award, Long> {
}
