package today.useit.linetracker.db.transforms;

import today.useit.linetracker.model.*;
import today.useit.linetracker.db.transforms.BaseLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javafx.util.Pair;

import com.google.cloud.bigquery.FieldValueList;
import com.google.gson.Gson;

public class DatedValueLoader extends BaseLoader<Pair<String, DatedValue>> {

  public DatedValueLoader(Gson gson) {
    super(gson);
  }

  public String getQuery() {
    return
      "SELECT v, __key__ \n" +
      "FROM `linetracking.datastore_backup.20221601_pertype_v` \n" +
      "LIMIT 10";
  }

  public Pair<String, DatedValue> transformRow(FieldValueList row) {
    double value = row.get("v").getDoubleValue();
    String path = (String) row.get("__key__").getRecordValue().get("path").getValue();
    String[] parts = path.split(", ");
    System.out.println(parts);
    String parent = parts[1].substring(1, parts[1].length() - 1);
    String timestamp = parts[3];

    String pattern = "ddMMyyyy";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    String dayString = simpleDateFormat.format(new Date(Long.parseLong(timestamp)));

    Pair<String, DatedValue> result =
      new Pair(parent, new DatedValue(dayString, value));
    System.out.println("Parsed: " + this.fmt(result));
    return result;
  }
}