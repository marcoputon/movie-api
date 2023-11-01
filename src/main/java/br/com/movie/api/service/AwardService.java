package br.com.movie.api.service;


import br.com.movie.api.model.Award;
import br.com.movie.api.repository.AwardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwardService {
    private final AwardRepository awardRepository;


    public Award findById(Long idAward) {
        return awardRepository.findById(idAward).orElseThrow();
    }
}
