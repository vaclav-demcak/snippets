package vd.sandbox.spring.amqp.api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;
import vd.sandbox.spring.amqp.AmqpMessagePublisher;
import vd.sandbox.spring.amqp.model.Notification;

@Controller
@RequestMapping("/amqp/${base-path:}")
public class MessageController implements MessageApi {

  private static final Logger LOG = LoggerFactory.getLogger(MessageController.class);

  private final NativeWebRequest request;

  @Autowired
  private AmqpMessagePublisher publisher;

  @Autowired
  public MessageController(NativeWebRequest request) {
    this.request = request;
  }

  @Override
  public Optional<NativeWebRequest> getRequest() {
    return Optional.ofNullable(request);
  }

  public ResponseEntity<String> ping(String ping) {
    return ResponseEntity.ok("pong to " + ping);
  }

  @Override
  public ResponseEntity sendMessageToTopic(String msg) {
    try {
      publisher.sendMsgToTopic(msg);
      return ResponseEntity.ok(Void.class);
    } catch (Exception e) {
      LOG.error("Unexpected exception ", e);
      return new ResponseEntity(e.getStackTrace(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity sendMessageToQueue(String msg) {
    try {
      publisher.sendMsgToClient(msg);
      return ResponseEntity.ok(Void.class);
    } catch (Exception e) {
      LOG.error("Unexpected exception ", e);
      return new ResponseEntity(e.getStackTrace(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity sendObjectToTopic(Notification msg) {
    try {
      publisher.sendNotificationToTopic(msg);
      return ResponseEntity.ok(Void.class);
    } catch (Exception e) {
      LOG.error("Unexpected exception ", e);
      return new ResponseEntity(e.getStackTrace(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity sendObjectToQueue(Notification msg) {
    try {
      publisher.sendNotificationToClient(msg);
      return ResponseEntity.ok(Void.class);
    } catch (Exception e) {
      LOG.error("Unexpected exception ", e);
      return new ResponseEntity(e.getStackTrace(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
