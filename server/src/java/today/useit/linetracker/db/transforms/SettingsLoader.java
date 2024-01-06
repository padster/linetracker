package today.useit.linetracker.db.transforms;

import today.useit.linetracker.model.*;
import com.google.cloud.bigquery.FieldValueList;
import com.google.gson.Gson;

public class SettingsLoader extends BaseLoader<Settings> {

  public SettingsLoader(Gson gson, String uid) {
    super(gson, uid);
  }

  public String getQuery() {
    return
      "SELECT DISTINCT gid, uid \n" +
      "FROM `linetracking.datastore_backup.20221601_pertype_h` \n" +
      "WHERE uid = \"" + this.uid + "\"";
  }

  public Settings transformRow(FieldValueList row) {
    // String uid = row.get("uid").getStringValue(); // TODO?
    String homeID = (String) row.get("gid").getValue();

    Settings result = new Settings(homeID);
    return result;
  }
}
