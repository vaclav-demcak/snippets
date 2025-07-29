package vd.sample.spring.mapstruct.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vd.sample.spring.mapstruct.repository.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT u.password FROM User u WHERE u.name = :userName")
    String findByUserName(@Param("userName") String userName);

    @Query(value = "SELECT u.id FROM User u WHERE u.name = :email")
    Long getIdByEmail(@Param("email") String email);

    @Query(value = "SELECT u FROM User u WHERE u.id = :id")
    User getUserById(@Param("id") Long id);
}
