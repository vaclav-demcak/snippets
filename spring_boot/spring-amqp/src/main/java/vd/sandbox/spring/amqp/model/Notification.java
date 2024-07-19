package vd.sandbox.spring.amqp.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

@ApiModel(description = "Notification je objekt zapuzdrujuci hodnoty Identifikator, To a Body")
public class Notification {

  @JsonProperty("ident")
  private Long ident;
  @JsonProperty("to")
  private String to;
  @JsonProperty("body")
  private String body;

  public Notification ident(Long ident) {
    this.ident = ident;
    return this;
  }

  public Notification to(String to) {
    this.to = to;
    return this;
  }

  public Notification body(String body) {
    this.body = body;
    return this;
  }

  public Notification() {
  }

  public Notification(Long ident, String to, String body) {
    this.ident = ident;
    this.to = to;
    this.body = body;
  }

  @ApiModelProperty(example = "5465464", required = true, value = "Identifikator osoby")
  public Long getIdent() { return ident; }

  public void setIdent(Long ident) { this.ident = ident; }

  @ApiModelProperty(example = "testerson@test.com", required = true, value = "Uzivatelsky email \"komu\" tj. TO")
  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  @ApiModelProperty(example = "Message Body means any text", required = true, value = "Telo spravy uzivatelovi")
  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Notification {\n");

    sb.append("    to: ").append(toIndentedString(to)).append("\n");
    sb.append("    body: ").append(toIndentedString(body)).append("\n");
    sb.append("    ident: ").append(toIndentedString(ident)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Notification notification = (Notification) o;
    return Objects.equals(this.ident, notification.ident) &&
        Objects.equals(this.to, notification.to) &&
        Objects.equals(this.body, notification.body);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ident, to, body);
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
