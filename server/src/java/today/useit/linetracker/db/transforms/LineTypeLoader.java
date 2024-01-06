package today.useit.linetracker.db.transforms;

import java.util.HashMap;
import java.util.Map;
import javafx.util.Pair;

import com.google.cloud.bigquery.FieldValueList;
import com.google.gson.Gson;

public class LineTypeLoader extends BaseLoader<Pair<String, String>> {

  private Map<String, String> cache = null;

  public LineTypeLoader(Gson gson, String uid) {
    super(gson, uid);
  }

  public String getQuery() {
    return
      "SELECT __key__.name id, IF(op is NULL, 'single', 'compos') type \n" +
      "FROM `linetracking.datastore_backup.20221601_pertype_l` \n" +
      "WHERE uid = \"" + this.uid + "\"";
  }

  public Pair<String, String> transformRow(FieldValueList row) {
    return new Pair<>(
      (String) row.get("id").getValue(),
      (String) row.get("type").getValue()
    );
  }

  public LineTypeLoader preload() {
    this.cache = new HashMap<String, String>();
    for (Pair<String, String> mapping : this.loadAll()) {
      this.cache.put(mapping.getKey(), mapping.getValue());
    }
    return this;
  }

  public String getTypeForID(String id) {
    if (this.cache == null) {
      throw new IllegalStateException("Preload first.");
    }
    if (!this.cache.containsKey(id)) {
      System.err.println("Unknown type for id: " + id);
      return null;
    }
    return this.cache.get(id);
  }
}
