package vd.sandbox.spring.swagger.api;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

@Controller
@RequestMapping("/hello/${base-path:}")
public class MessageController implements MessageApi {

  private final NativeWebRequest request;

  @Autowired
  public MessageController(NativeWebRequest request) {
    this.request = request;
  }

  @Override
  public Optional<NativeWebRequest> getRequest() {
    return Optional.ofNullable(request);
  }

  public ResponseEntity printMessage(String msg) {
    String result = "Hello " + msg + "!";
    return ResponseEntity.ok(result);
  }
}
