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
import org.springframework.web.multipart.MultipartFile;
import vd.sample.spring.mapstruct.api.TravelingServicesApi;
import vd.sample.spring.mapstruct.model.InitUploadSessionDto;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("${openapi.aCGAPIDocumentation.base-path:}")
public class TravelingApiController implements TravelingServicesApi {

    private static final Logger LOG = LoggerFactory.getLogger(TravelingApiController.class);

    private final NativeWebRequest request;
    @Value("${spring.servlet.multipart.location}")
    private String defaultTempPath;

    @Autowired
    public TravelingApiController(NativeWebRequest request) {
        this.request = request;
    }


    @Override
    public ResponseEntity<InitUploadSessionDto> initiateSession() throws Exception {
        LOG.info("initSession for upload file method call ...");
        return TravelingServicesApi.super.initiateSession();
    }

    @Override
    public ResponseEntity<Void> uploadFile(String xSessionId, MultipartFile fileName) throws Exception {
        LOG.info("uploadFile method call with Session ID {}", xSessionId);

//        String uploadDir = "/var/tmp/uploads";
        Path path = Paths.get(defaultTempPath, fileName.getOriginalFilename());

        Files.createDirectories(path.getParent());
        Files.copy(fileName.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

//
//        TradacomsTranformer tr = new TradacomsTranformer();
//        tr.writeJsonToLog(fileName.getInputStream());

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


    @Override
    public ResponseEntity<Void> finalizeUpload(String xSessionId) throws Exception {
        LOG.info("finalize upload file method call with session ID {}", xSessionId);
        return TravelingServicesApi.super.finalizeUpload(xSessionId);
    }
}
