package vd.sandbox.spring.jpa.invoker;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.databind.util.ISO8601Utils;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import java.text.FieldPosition;
import java.util.Date;


public class RFC3339DateFormat extends /*ISO8601DateFormat*/ StdDateFormat {

  private static final long serialVersionUID = 1L;

  // Same as ISO8601DateFormat but serializing milliseconds.
  @Override
  public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
    String value = StdDateFormat.DATE_FORMAT_ISO8601.format(date);
//    String value = ISO8601Utils.format(date, true);
    toAppendTo.append(value);
    return toAppendTo;
  }

}