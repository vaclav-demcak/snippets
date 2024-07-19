package vd.sandbox.spring.swagger.api;

import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

@Api(value = "hello", description = "Endpoint for Hello specific operations")
public interface MessageApi {

  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  @ApiOperation(value = "Returns param", nickname = "printMessage", notes = "Returns param", response = String.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successful retrieval of param value", response = String.class) })
  @RequestMapping(value = "/{msg}",
      produces = { "application/json" },
      method = RequestMethod.GET)
  default ResponseEntity<String> printMessage(@ApiParam(value = "",required=true) @PathVariable("msg") String msg) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
