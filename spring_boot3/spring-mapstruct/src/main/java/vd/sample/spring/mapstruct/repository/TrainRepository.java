package vd.sample.spring.mapstruct.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vd.sample.spring.mapstruct.repository.entity.Train;
import vd.sample.spring.mapstruct.repository.entity.User;

import java.util.List;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {


    @Query("SELECT t FROM Train t WHERE t.name = :name")
    List<Train> findByName(@Param("name") String name);

    @Query(value = "SELECT t FROM Train t WHERE t.id = :id")
    Train getTrainById(@Param("id") Long id);
}
