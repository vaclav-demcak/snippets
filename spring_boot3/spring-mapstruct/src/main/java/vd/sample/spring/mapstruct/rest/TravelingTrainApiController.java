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
import vd.sample.spring.mapstruct.api.TrainServicesApi;
import vd.sample.spring.mapstruct.model.TrainDto;
import vd.sample.spring.mapstruct.model.TrainIdentDto;
import vd.sample.spring.mapstruct.service.TrainService;

import java.util.List;

@Controller
@RequestMapping("${openapi.aCGAPIDocumentation.base-path:}")
public class TravelingTrainApiController implements TrainServicesApi {

    private static final Logger LOG = LoggerFactory.getLogger(TravelingTrainApiController.class);

    private final NativeWebRequest request;
    @Value("${spring.servlet.multipart.location}")
    private String defaultTempPath;

    @Autowired
    private TrainService trainService;

    @Autowired
    public TravelingTrainApiController(NativeWebRequest request) {
        this.request = request;
    }


    @Override
    public ResponseEntity<TrainIdentDto> createTrain(TrainDto trainDto) throws Exception {
        LOG.info("createTrain method call {} ...", trainDto.toString());
        TrainIdentDto result = trainService.save(trainDto);
        return ResponseEntity.ok(result);
    }


    @Override
    public ResponseEntity<TrainDto> getTrainById(Long id) throws Exception {
        LOG.info("getTrainById method call {} ...", id);
        TrainDto result = trainService.getById(id);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Void> updateTrain(TrainDto trainDto) throws Exception {
        LOG.info("updateTrain method call {} ...", trainDto.toString());
        trainService.save(trainDto);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<Void> deleteTrainById(Long id) throws Exception {
        LOG.info("deleteTrainById method call {} ...", id);
        trainService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<List<TrainDto>> findTrain(TrainDto trainDto) throws Exception {
        LOG.info("findTrain method call {} ...", trainDto.toString());
        List<TrainDto> result = trainService.getAll();
        return ResponseEntity.ok(result);
    }
}
