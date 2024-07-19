package vd.samples.springboot.jasypt.rest;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import vd.samples.springboot.jasypt.api.AppInfoApi;
import vd.samples.springboot.jasypt.model.ApplicationBuildInfoDto;

@Controller
@RequestMapping("${openapi.springJPASampleUserAPIDocumentation.base-path:}")
public class ApiController_AppInfo implements AppInfoApi {

    private static final Logger LOG = LoggerFactory.getLogger(ApiController_AppInfo.class);

    private final NativeWebRequest request;

    @Autowired
    private BuildProperties buildProperties;


    @Autowired
    public ApiController_AppInfo(NativeWebRequest request) {
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
}
