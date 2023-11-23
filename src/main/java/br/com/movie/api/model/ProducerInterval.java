package br.com.movie.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProducerInterval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducerInterval;

    private String desProducer;
    private Integer numYearStart;
    private Integer numYearEnd;
    private Integer numInterval;
}
