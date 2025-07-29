package vd.sample.spring.mapstruct.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import vd.sample.spring.mapstruct.model.UserIdentDto;
import vd.sample.spring.mapstruct.repository.entity.User;
import vd.sample.spring.mapstruct.service.mapper.UserMapper;
import vd.sample.spring.mapstruct.model.UserDto;
import vd.sample.spring.mapstruct.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepo;
    public UserService(UserRepository userRepository) {
        this.userRepo = userRepository;
    }

    public String getPasswordByUserName(String userName) {
        return userRepo.findByUserName(userName);
    }

    public UserIdentDto save(UserDto user) {
        User result = userRepo.save(UserMapper.INSTANCE.mapEntityFromDto(user));
        return UserMapper.INSTANCE.mapDtoIdentFromEntity(result);
    }

    public List<User> getAll() {
        return userRepo.findAll();
    }

    public Long getIdByEmail(String email) {
        return userRepo.getIdByEmail(email);
    }

    public UserDto getById(Long id) {
        User user = userRepo.getUserById(id);
        return UserMapper.INSTANCE.mapDtoFromEntity(user);
    }

    public void deleteById(Long id) {
        userRepo.deleteById(id);
    }
}
