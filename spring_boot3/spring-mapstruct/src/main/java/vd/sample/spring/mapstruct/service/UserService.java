package vd.sample.spring.mapstruct.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import vd.sample.spring.mapstruct.entity.User;
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

    public void save(User user) {
        userRepo.save(user);
    }

    public List<User> getAll() {
        return userRepo.findAll();
    }

    public Long getIdByEmail(String email) {
        return userRepo.getIdByEmail(email);
    }

    public User getById(Long id) {
        return userRepo.getUserById(id);
    }
}
