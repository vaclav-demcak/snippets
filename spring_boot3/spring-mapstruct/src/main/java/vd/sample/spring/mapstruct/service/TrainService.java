package vd.sample.spring.mapstruct.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import vd.sample.spring.mapstruct.model.TrainDto;
import vd.sample.spring.mapstruct.model.TrainIdentDto;
import vd.sample.spring.mapstruct.model.UserDto;
import vd.sample.spring.mapstruct.model.UserIdentDto;
import vd.sample.spring.mapstruct.repository.UserRepository;
import vd.sample.spring.mapstruct.repository.entity.Train;
import vd.sample.spring.mapstruct.repository.TrainRepository;
import vd.sample.spring.mapstruct.repository.entity.User;
import vd.sample.spring.mapstruct.service.mapper.TrainMapper;
import vd.sample.spring.mapstruct.service.mapper.UserMapper;

import java.util.ArrayList;
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

    public TrainIdentDto save(TrainDto trainDto) {
        Train result = trainRepo.save(TrainMapper.INSTANCE.mapEntityFromDto(trainDto));
        return TrainMapper.INSTANCE.mapDtoIdentFromEntity(result);
    }

    public List<TrainDto> getAll() {
        List<TrainDto> result = new ArrayList<>(1);
        trainRepo.findAll().forEach(t -> result.add(TrainMapper.INSTANCE.mapDtoFromEntity(t)));
        return result;
    }

    public TrainDto getById(Long id) {
        Train train = trainRepo.getTrainById(id);
        return TrainMapper.INSTANCE.mapDtoFromEntity(train);
    }

    public void deleteById(Long id) {
        trainRepo.deleteById(id);
    }
}
