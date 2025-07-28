package vd.sample.spring.mapstruct.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import vd.sample.spring.mapstruct.api.AppInfoApi;
import vd.sample.spring.mapstruct.model.ApplicationBuildInfoDto;
import vd.sample.spring.mapstruct.model.ApplicationBuildInfoResponseDto;

import java.util.Optional;

@Controller
@RequestMapping("${openapi.aCGAPIDocumentation.base-path:}")
public class AppInfoApiController implements AppInfoApi {

    private static final Logger LOG = LoggerFactory.getLogger(AppInfoApiController.class);

    private final NativeWebRequest request;

    @Autowired
    private BuildProperties buildProperties;

    @Value("${git.commit.id}")
    private String commitId;

    @Value("${git.build.user.name}")
    private String buildUser;


    @Autowired
    public AppInfoApiController(NativeWebRequest request) {
        this.request = request;
    }


    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }


    @Override
    public ResponseEntity<ApplicationBuildInfoDto> buildInfo() {
        LOG.info("buildInfo call ...");
        ApplicationBuildInfoDto dto = new ApplicationBuildInfoDto();
        dto.setName(buildProperties.getName());
        dto.setVersion(buildProperties.getVersion());
        dto.setBuildedIn(buildProperties.getTime().toString());
        dto.setArifactGroup(buildProperties.getGroup());
        dto.setArtifactName(buildProperties.getArtifact());
        dto.setGitHash(commitId);
        dto.setUserName(buildUser);
        return ResponseEntity.ok(dto);
    }

    public ResponseEntity<ApplicationBuildInfoResponseDto> buildUserInfo() {
        LOG.info("buildUserInfo call ...");
        ApplicationBuildInfoResponseDto dto = new ApplicationBuildInfoResponseDto();
        dto.setName(buildProperties.getName());
        dto.setVersion(buildProperties.getVersion());
        dto.setUserName(buildUser);
        return ResponseEntity.ok(dto);
    }
}
