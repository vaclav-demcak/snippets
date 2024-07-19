package vd.samples.springboot.jasypt.dao;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vd.samples.springboot.jasypt.dao.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

  @Query("FROM User WHERE userFirstName LIKE CONCAT('%',:firstName,'%')")
  List<User> findByFistName(@Param("firstName") String firstName);
}
