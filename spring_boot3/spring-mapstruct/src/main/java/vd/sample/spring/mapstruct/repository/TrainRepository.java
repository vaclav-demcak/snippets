package vd.sample.spring.mapstruct.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vd.sample.spring.mapstruct.entity.Train;

import java.util.List;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {

    @Query("SELECT t FROM Train b WHERE b.name = :name")
    List<Train> findByName(@Param("name") String name);
}
