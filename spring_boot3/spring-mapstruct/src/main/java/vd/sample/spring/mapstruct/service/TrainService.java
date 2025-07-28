package vd.sample.spring.mapstruct.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import vd.sample.spring.mapstruct.entity.Train;
import vd.sample.spring.mapstruct.repository.TrainRepository;

import java.util.List;

@Service
public class TrainService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainService.class);

    private final TrainRepository trainRepo;

    public TrainService(TrainRepository trainRepository) {
        this.trainRepo = trainRepository;
    }

    public List<Train> getByName(String name) {
        return trainRepo.findByName(name);
    }
}
