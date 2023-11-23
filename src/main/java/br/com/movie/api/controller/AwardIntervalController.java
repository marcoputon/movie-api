package br.com.movie.api.controller;

import br.com.movie.api.service.IntervalService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/award-intervals")
public class AwardIntervalController {
    private final IntervalService intervalService;

    private static final String GENERIC_ERROR = "Erro ao processar: %s";


    @GetMapping()
    public ResponseEntity<?> findMaxAndMinAwardInterval() {
        try {
            return ResponseEntity.ok().body(intervalService.findMinAndMaxIntervals());
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.unprocessableEntity().body(String.format(GENERIC_ERROR, e.getMessage()));
        }
    }
}
