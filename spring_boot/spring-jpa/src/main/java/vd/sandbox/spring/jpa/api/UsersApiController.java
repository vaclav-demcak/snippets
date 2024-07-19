package vd.sandbox.spring.jpa.api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;
import java.util.Optional;
import vd.sandbox.spring.jpa.model.CreateUserRequestDto;
import vd.sandbox.spring.jpa.model.UserResponseDto;

@Controller
@RequestMapping("${openapi.springJPASampleUserAPIDocumentation.base-path:}")
public class UsersApiController implements UsersApi {

    @Autowired
    private UserServices services;

    private final NativeWebRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    public ResponseEntity<List<UserResponseDto>> v1UsersGet() {
        try {
            return ResponseEntity.ok(services.getUsers());
        } catch (Exception ex) {
            return new ResponseEntity(ex.getStackTrace(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<UserResponseDto> v1UsersPost(CreateUserRequestDto user) {
        try {
            return ResponseEntity.of(services.add(user));
        } catch (Exception ex) {
            return new ResponseEntity(ex.getStackTrace(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<UserResponseDto>> v1UsersUserNameGet(String userName) {
        try {
            return ResponseEntity.ok(services.getUsersByName(userName));
        } catch (Exception e) {
            return new ResponseEntity(e.getStackTrace(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Boolean> v1UsersDelete(Long id) {
        try {
            services.delete(id);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return new ResponseEntity(e.getStackTrace(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
