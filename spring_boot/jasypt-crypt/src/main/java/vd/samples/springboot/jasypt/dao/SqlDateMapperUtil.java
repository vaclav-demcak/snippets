package vd.samples.springboot.jasypt.dao;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class SqlDateMapperUtil {

  public java.util.Date asDate(LocalDate date) {
    return date != null ? java.util.Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
  }

  public LocalDate asLocalDate(java.util.Date date) {
    if (date == null) {
      return null;
    }
    if (date instanceof java.sql.Date) {
      return ((java.sql.Date) date).toLocalDate();
    } else {
      return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
  }

  public java.util.Date asDate(OffsetDateTime date) {
    return date != null ? java.util.Date.from(date.atZoneSimilarLocal(ZoneId.systemDefault()).toInstant()) : null;
  }

  public OffsetDateTime asOffsetDateTime(java.util.Date date) {
    if (date == null) {
      return null;
    }
    if (date instanceof java.sql.Date) {
      return ((java.sql.Date) date).toInstant().atOffset(ZoneOffset.UTC);
    } else {
      return OffsetDateTime.from(date.toInstant());
    }
  }
}
