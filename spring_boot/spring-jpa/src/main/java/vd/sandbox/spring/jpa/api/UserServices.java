package vd.sandbox.spring.jpa.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vd.sandbox.spring.jpa.db.model.User;
import vd.sandbox.spring.jpa.db.repo.UserRepository;
import vd.sandbox.spring.jpa.model.UserDto;
import vd.sandbox.spring.jpa.model.UserResponseDto;

@Component
public class UserServices {

  @Autowired
  private UserRepository userRepo;

  public Optional<UserResponseDto> add(UserDto dto) {
    User user = userRepo.save(UserMapper.INSTANCE.mapToEntity(dto));
    return user != null ? Optional.of(UserMapper.INSTANCE.mapToResponseDto(user)):Optional.empty();
  }

  public void delete(long id) {
    userRepo.deleteById(id);
  }

  public Optional<UserDto> getUserById(long id) {
    Optional<User> user = userRepo.findById(id);
    return user.isPresent() ? Optional.of(UserMapper.INSTANCE.mapToDto(user.get())) : Optional.empty();
  }

  public List<UserResponseDto> getUsers() {
    List<UserResponseDto> users = new ArrayList<>(1);
    userRepo.findAll().forEach(u -> users.add(UserMapper.INSTANCE.mapToResponseDto(u)));
    return users;
  }

  public List<UserResponseDto> getUsersByName(String userName) {
    List<UserResponseDto> users = new ArrayList<>(1);
    userRepo.findByFistName(userName).forEach(u -> users.add(UserMapper.INSTANCE.mapToResponseDto(u)));
    return users;
  }
}
