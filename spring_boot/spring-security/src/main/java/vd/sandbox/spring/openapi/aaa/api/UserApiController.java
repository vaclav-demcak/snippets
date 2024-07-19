package vd.sandbox.spring.openapi.aaa.api;

import java.util.Optional;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import vd.sandbox.spring.openapi.aaa.api.repo.UserRepository;
import vd.sandbox.spring.openapi.aaa.model.ApplicationInfoDto;

@Controller
@RequestMapping("${openapi.aPISecurityServicesDocumentation.base-path:/aaa}")
public class UserApiController implements UserApi {

    private static final Logger LOG = LoggerFactory.getLogger(UserApiController.class);

    @Autowired
    private UserRepository repo;

    private final NativeWebRequest request;

    @Autowired
    public UserApiController(NativeWebRequest request) {
        this.request = request;
    }

    @PostConstruct
    public void postInit() {
        LOG.info("UserApiController was initialized successfully...");
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<ApplicationInfoDto> info() {
        LOG.trace("info method call ...");
        return ResponseEntity.ok(repo.userInfo());
    }

    @Override
    public ResponseEntity<String> welcome() {
        LOG.trace("welcome method call ...");
        return ResponseEntity.ok(repo.userWelcomeMsg());
    }
}
