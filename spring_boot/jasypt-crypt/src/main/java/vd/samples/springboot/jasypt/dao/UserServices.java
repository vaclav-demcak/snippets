package vd.samples.springboot.jasypt.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vd.samples.springboot.jasypt.dao.entity.User;
import vd.samples.springboot.jasypt.dao.entity.UserMapper;
import vd.samples.springboot.jasypt.model.UserDto;
import vd.samples.springboot.jasypt.model.UserResponseDto;

@Component
public class UserServices {

  @Autowired
  private UserRepository userRepo;

  public Optional<UserResponseDto> add(UserDto dto) {
    User user = userRepo.save(UserMapper.INSTANCE.mapToEntity(dto));
    return Optional.of(UserMapper.INSTANCE.mapToResponseDto(user));
  }

  public void delete(long id) {
    userRepo.deleteById(id);
  }

  public Optional<UserDto> getUserById(long id) {
    Optional<User> user = userRepo.findById(id);
    return user.map(UserMapper.INSTANCE::mapToDto);
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
