package br.com.movie.api.service;

import br.com.movie.api.model.Producer;
import br.com.movie.api.repository.ProducerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerService {
    private final ProducerRepository producerRepository;


    public void saveProducer(Producer producer) {
        producerRepository.saveAndFlush(producer);
    }


    public List<String> findProducersWithMultipleAwards() {
        return producerRepository.findProducersWithMultipleAwards();
    }


    public List<Integer> findYearsByDesProducer(String desProducer) {
        return producerRepository.findYearsByDesProducer(desProducer);
    }
}
