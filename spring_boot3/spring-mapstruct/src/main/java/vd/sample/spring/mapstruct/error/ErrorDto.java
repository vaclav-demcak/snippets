package vd.sample.spring.mapstruct.error;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class ErrorDto {
  @JsonProperty("code")
  private String code;

  @JsonProperty("message")
  private String message;

  public ErrorDto code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Error code
   * @return code
   */
//  @Schema(name = "code", description = "Error code", required = true)
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public ErrorDto message(String message) {
    this.message = message;
    return this;
  }

  /**
   * The cause of the Error.
   * @return message
   */
//  @Schema(name = "message", description = "The cause of the Error.", required = true)
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ErrorDto error = (ErrorDto) o;
    return Objects.equals(this.code, error.code) &&
        Objects.equals(this.message, error.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ErrorDto {\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
