package vd.sample.spring.mapstruct.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vd.sample.spring.mapstruct.entity.Passenger;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {

//    @Modifying
//    @Query("UPDATE Passenger SET price = price - :linePrice, lastBus = :pageName WHERE userId = :userId")
//    void payForBusByIdAndBusName(String pageName, double linePrice, Long userId);

    @Query(value = "SELECT p FROM Passenger p WHERE p.userId = :id")
    Passenger getPassengerById(Long id);

    @Query(value = "SELECT p FROM Passenger p WHERE p.userId = :l")
    Passenger findByUserId(long l);

    @Query(value = "SELECT p FROM Passenger p WHERE p.price - (SELECT b.linePrice FROM Train b WHERE b.name = :lineName) > 0 AND p.userId = :userId")
    Passenger checkDB(Long userId, String lineName);

    @Modifying
    @Query("UPDATE Passenger p SET p.price = CASE WHEN (p.price + 100) <= 500 THEN (p.price + 100) ELSE p.price END WHERE p.userId = :userId")
    void loadMoney(Long userId);
}
