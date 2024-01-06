package today.useit.linetracker.db.transforms;

import today.useit.linetracker.model.*;
import com.google.cloud.bigquery.FieldValueList;
import com.google.gson.Gson;

public class SingleLineLoader extends BaseLoader<SingleLineMeta> {

  public SingleLineLoader(Gson gson, String uid) {
    super(gson, uid);
  }

  public String getQuery() {
    return
      "SELECT __key__, uid, name, link \n" +
      "FROM `linetracking.datastore_backup.20221601_pertype_l` \n" +
      "WHERE op IS NULL AND uid = \"" + this.uid + "\"";
  }

  public SingleLineMeta transformRow(FieldValueList row) {
    String id = this.getId(row);
    // String uid = row.get("uid").getStringValue(); // TODO
    String name = (String) row.get("name").getValue();
    String link = (String) row.get("link").getValue();

    SingleLineMeta result = new SingleLineMeta();
    result.setId(id);
    result.setName(name);
    result.setLink(link);
    return result;
  }
}
