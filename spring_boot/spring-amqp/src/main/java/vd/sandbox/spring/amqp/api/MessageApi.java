package vd.sandbox.spring.amqp.api;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;
import vd.sandbox.spring.amqp.AmqpMessagePublisher;
import vd.sandbox.spring.amqp.model.Notification;

@Api(value = "amqp", description = "Endpoint for Hello specific operations")
public interface MessageApi {

  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  @ApiOperation(value = "Returns param", nickname = "ping", notes = "Returns param", response = String.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successful retrieval of pong value", response = String.class) })
  @RequestMapping(value = "/{msg}",
      produces = { "application/json" },
      method = RequestMethod.GET)
  default ResponseEntity<String> ping(@ApiParam(value = "",required=true) @PathVariable("msg") String msg) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @ApiOperation(value = "Send msgToTopic", nickname = "sendMessageToTopic", notes = "")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successful message send to Topic") })
  @RequestMapping(value = "/msgToTopic",
      produces = { "application/json" },
      method = RequestMethod.PUT)
  default ResponseEntity<Void> sendMessageToTopic(@ApiParam(value = "" ) @RequestBody(required=true) String msg) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @ApiOperation(value = "Send msgToQueue", nickname = "sendMessageToQueue", notes = "")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successful message send to Queue") })
  @RequestMapping(value = "/msgToQueue",
      produces = { "application/json" },
      method = RequestMethod.PUT)
  default ResponseEntity<Void> sendMessageToQueue(@ApiParam(value = "" ) @RequestBody(required = true) String msg) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @ApiOperation(value = "Send objectToTopic", nickname = "sendObjectToTopic", notes = "")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successful notification send to Topic") })
  @RequestMapping(value = "/objectToTopic",
      produces = { "application/json" },
      method = RequestMethod.PUT)
  default ResponseEntity<Void> sendObjectToTopic(@ApiParam(value = "Notification to Topic", required = true, defaultValue = "null") Notification msg) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @ApiOperation(value = "Send objectToQueue", nickname = "sendObjectToQueue", notes = "")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successful notification send to Queue") })
  @RequestMapping(value = "/objectToQueue",
      produces = { "application/json" },
      method = RequestMethod.PUT)
  default ResponseEntity<Void> sendObjectToQueue(@ApiParam(value = "Notification to Queue", required = true, defaultValue = "null") Notification msg) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
