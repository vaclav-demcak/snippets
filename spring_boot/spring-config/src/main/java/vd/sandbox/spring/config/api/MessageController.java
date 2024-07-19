package vd.sandbox.spring.config.api;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

@Controller
@RequestMapping("/config/${base-path:}")
public class MessageController implements MessageApi {

  private final NativeWebRequest request;

  @Autowired
  private BuildProperties buildProperties;

  @Autowired
  public MessageController(NativeWebRequest request) {
    this.request = request;
  }

  @Override
  public Optional<NativeWebRequest> getRequest() {
    return Optional.ofNullable(request);
  }

  public ResponseEntity<String> applicationInformation() {
    String newLine = System.getProperty("line.separator");
    StringBuilder sb = new StringBuilder();
    sb.append("Applicaction Name: ").append(buildProperties.getName()).append(newLine);
    sb.append("Version: ").append(buildProperties.getVersion()).append(newLine);
    sb.append("Builded in: ").append(buildProperties.getTime()).append(newLine);
    sb.append("Group: ").append(buildProperties.getGroup()).append(newLine);
    sb.append("Artifact: ").append(buildProperties.getArtifact()).append(newLine);
    sb.append("git hash: ").append(buildProperties.get("git.hash")).append(newLine);
    sb.append("Build User: ").append(buildProperties.get("builded.by")).append(newLine);
    return ResponseEntity.ok(sb.toString());
  }

  public ResponseEntity printMessage(String msg) {
    String result = "Hello " + msg + "!";
    return ResponseEntity.ok(result);
  }
}
