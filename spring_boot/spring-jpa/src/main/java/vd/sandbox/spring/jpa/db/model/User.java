package vd.sandbox.spring.jpa.db.model;

import java.sql.Date;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "User")
public class User {

  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "validFrom", columnDefinition="DATETIME", nullable = true)
  private Timestamp validFrom;

  @Column(name = "userFirstName", length = 100, nullable = false)
  private String userFirstName;

  @Column(name = "userLastName", length = 100, nullable = false)
  private String userLastName;

  @Column(name = "userMiddleName", length = 100, nullable = false)
  private String userMiddleName;

  @Column(name = "userEmail", length = 100, nullable = false)
  private String userEmail;

  @Column(name = "userName", length = 100, nullable = false)
  private String userName;

  @Column(name = "password", length = 100, nullable = false)
  private String password;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUserFirstName() {
    return userFirstName;
  }

  public void setUserFirstName(String userFirstName) {
    this.userFirstName = userFirstName;
  }

  public String getUserLastName() {
    return userLastName;
  }

  public void setUserLastName(String userLastName) {
    this.userLastName = userLastName;
  }

  public String getUserMiddleName() {
    return userMiddleName;
  }

  public void setUserMiddleName(String userMiddleName) {
    this.userMiddleName = userMiddleName;
  }

  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Timestamp getValidFrom() { return validFrom; }

  public void setValidFrom(Timestamp validFrom) { this.validFrom = validFrom; }
}
