package vd.sandbox.spring.jpa.db.repo;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vd.sandbox.spring.jpa.db.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

  @Query("FROM User WHERE userFirstName LIKE CONCAT('%',:firstName,'%')")
  List<User> findByFistName(@Param("firstName") String firstName);
}
