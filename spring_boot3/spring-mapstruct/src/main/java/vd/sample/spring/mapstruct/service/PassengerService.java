package vd.sample.spring.mapstruct.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import vd.sample.spring.mapstruct.entity.Passenger;
import vd.sample.spring.mapstruct.repository.PassengerRepository;
import vd.sample.spring.mapstruct.repository.TrainRepository;

@Service
public class PassengerService {

    private static final Logger LOG = LoggerFactory.getLogger(PassengerService.class);

    private final PassengerRepository passengerRepo;
    private final TrainRepository trainRepo;

    public PassengerService(TrainRepository trainRepository, PassengerRepository passengerRepository) {
        this.passengerRepo = passengerRepository;
        this.trainRepo = trainRepository;
    }

    public void payForBus(String pageName, Long userId) {

        double linePrice = trainRepo.findByName(pageName).get(0).getLinePrice();
//        passengerRepo.payForBusByIdAndBusName(pageName, linePrice, userId);
    }

    public double getCash(Long userId) {
        return passengerRepo.findByUserId(userId).getPrice();
    }

//    public String getLastTrainName(Long userId) {
//        return passengerRepo.findByUserId(userId).get();
//    }

    public void save(Passenger samplePassenger) {
        passengerRepo.save(samplePassenger);
    }

    public Passenger getById(Long id) {
        return passengerRepo.getPassengerById(id);
    }

    public boolean checkAmount(Long userId, String pageName) {
        Passenger passenger = passengerRepo.checkDB(userId, pageName);
        if (passenger == null) {
            return false;
        } else {
            return true;
        }
    }

    @Transactional
    public void loadMoney(Long userId) {
        passengerRepo.loadMoney(userId);
    }
}
