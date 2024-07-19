package vd.samples.springboot.jasypt.rest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import java.util.Optional;
import vd.samples.springboot.jasypt.api.UsersApi;
import vd.samples.springboot.jasypt.model.CreateUserRequestDto;
import vd.samples.springboot.jasypt.model.UserResponseDto;
import vd.samples.springboot.jasypt.dao.UserServices;

@Controller
@RequestMapping("${openapi.springJPASampleUserAPIDocumentation.base-path:}")
public class ApiController_User implements UsersApi {

    @Autowired
    private UserServices services;

    private final NativeWebRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public ApiController_User(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        try {
            return ResponseEntity.ok(services.getUsers());
        } catch (Exception ex) {
            return new ResponseEntity(ex.getStackTrace(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public ResponseEntity<UserResponseDto> createUser(CreateUserRequestDto user) {
        try {
            return ResponseEntity.of(services.add(user));
        } catch (Exception ex) {
            return new ResponseEntity(ex.getStackTrace(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public ResponseEntity<List<UserResponseDto>> getUserByFirstName(String userName) {
        try {
            return ResponseEntity.ok(services.getUsersByName(userName));
        } catch (Exception e) {
            return new ResponseEntity(e.getStackTrace(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public ResponseEntity<Boolean> deleteUser(Long id) {
        try {
            services.delete(id);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return new ResponseEntity(e.getStackTrace(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
