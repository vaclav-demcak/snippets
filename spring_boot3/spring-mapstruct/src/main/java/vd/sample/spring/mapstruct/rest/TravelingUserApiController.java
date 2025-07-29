package vd.sample.spring.mapstruct.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import vd.sample.spring.mapstruct.api.UserServicesApi;
import vd.sample.spring.mapstruct.model.UserDto;
import vd.sample.spring.mapstruct.model.UserIdentDto;
import vd.sample.spring.mapstruct.service.UserService;

@Controller
@RequestMapping("${openapi.aCGAPIDocumentation.base-path:}")
public class TravelingUserApiController implements UserServicesApi {

    private static final Logger LOG = LoggerFactory.getLogger(TravelingUserApiController.class);

    private final NativeWebRequest request;
    @Value("${spring.servlet.multipart.location}")
    private String defaultTempPath;

    @Autowired
    private UserService userService;

    @Autowired
    public TravelingUserApiController(NativeWebRequest request) {
        this.request = request;
    }


    @Override
    public ResponseEntity<UserIdentDto> createUser(UserDto userDto) throws Exception {
        LOG.info("createUser method call {} ...", userDto.toString());
        UserIdentDto result = userService.save(userDto);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<UserDto> getUserById(Long id) throws Exception {
        LOG.info("getUserById method call {} ...", id);
        UserDto result = userService.getById(id);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Void> updateUser(UserDto userDto) throws Exception {
        LOG.info("updateUser method call {} ...", userDto.toString());
        userService.save(userDto);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<Void> deleteUserById(Long id) throws Exception {
        LOG.info("deleteUserById method call {} ...", id);
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
