package today.useit.linetracker.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateFormat {
  public static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMdd");

  public static long dateToMs(String yyyymmdd) throws ParseException {
    // NOTE: Not timezone aware, so only use to compare dates.
    return FORMAT.parse(yyyymmdd).getTime();
  }
}
