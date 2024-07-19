package vd.samples.springboot.oracle.rest;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import vd.samples.springboot.oracle.api.AppInfoApi;
import vd.samples.springboot.oracle.model.ApplicationBuildInfoDto;
import vd.samples.springboot.oracle.model.ApplicationBuildInfoResponseDto;

@Controller
@RequestMapping("${openapi.aCGAPIDocumentation.base-path:}")
public class AppInfoController implements AppInfoApi {

    private static final Logger LOG = LoggerFactory.getLogger(AppInfoController.class);

    private final NativeWebRequest request;

    @Autowired
    private BuildProperties buildProperties;


    @Autowired
    public AppInfoController(NativeWebRequest request) {
        this.request = request;
    }


    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }


    @Override
    public ResponseEntity<ApplicationBuildInfoDto> buildInfo() {
        LOG.info("info call ...");
        ApplicationBuildInfoDto dto = new ApplicationBuildInfoDto();
        dto.setName(buildProperties.getName());
        dto.setVersion(buildProperties.getVersion());
        dto.setBuildedIn(buildProperties.getTime().toString());
        dto.setArifactGroup(buildProperties.getGroup());
        dto.setArtifactName(buildProperties.getArtifact());
        dto.setGitHash(buildProperties.get("git.hash"));
        dto.setUserName(buildProperties.get("builded.by"));
        return ResponseEntity.ok(dto);
    }

    public ResponseEntity<ApplicationBuildInfoResponseDto> buildUserInfo() {
        LOG.info("info call ...");
        ApplicationBuildInfoResponseDto dto = new ApplicationBuildInfoResponseDto();
        dto.setName(buildProperties.getName());
        dto.setVersion(buildProperties.getVersion());
        dto.setUserName(buildProperties.get("builded.by"));
        return ResponseEntity.ok(dto);
    }
}
