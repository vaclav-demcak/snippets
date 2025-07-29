package vd.sample.spring.mapstruct.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vd.sample.spring.mapstruct.repository.entity.Passenger;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {

//    @Modifying
//    @Query("UPDATE Passenger SET price = price - :linePrice, lastBus = :pageName WHERE userId = :userId")
//    void payForBusByIdAndBusName(String pageName, double linePrice, Long userId);

    @Query(value = "SELECT p FROM Passenger p WHERE p.userId = :id")
    Passenger getPassengerById(@Param("id") Long id);

    @Query(value = "SELECT p FROM Passenger p WHERE p.userId = :userId")
    Passenger findByUserId(@Param("userId") long userId);

    @Query(value = "SELECT p FROM Passenger p WHERE p.price - (SELECT b.linePrice FROM Train b WHERE b.name = :lineName) > 0 AND p.userId = :userId")
    Passenger checkDB(@Param("userId") Long userId, @Param("lineName") String lineName);

    @Modifying
    @Query("UPDATE Passenger p SET p.price = CASE WHEN (p.price + 100) <= 500 THEN (p.price + 100) ELSE p.price END WHERE p.userId = :userId")
    void loadMoney(@Param("userId") Long userId);
}
