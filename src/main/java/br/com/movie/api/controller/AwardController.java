package br.com.movie.api.controller;

import br.com.movie.api.model.Award;
import br.com.movie.api.service.AwardService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/award")
public class AwardController {
    private final AwardService awardService;


    @GetMapping(value = "/{idAward}")
    public ResponseEntity<Award> findAwardById(
            @PathVariable(value = "idAward") @NotNull Long idAward
    ) {
        return ResponseEntity.ok().body(awardService.findById(idAward));
    }

}
