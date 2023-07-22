package today.useit.linetracker.db.transforms;

import today.useit.linetracker.model.*;
import today.useit.linetracker.db.transforms.BaseLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.util.Pair;

import com.google.cloud.bigquery.FieldValueList;
import com.google.gson.Gson;

public class LineTypeLoader extends BaseLoader<Pair<String, String>> {

  private Map<String, String> cache = null;

  public LineTypeLoader(Gson gson) {
    super(gson);
  }

  public String getQuery() {
    return
      "SELECT __key__.name id, IF(op is NULL, 's', 'c') type \n" +
      "FROM `linetracking.datastore_backup.20221601_pertype_l` ";
  }

  public Pair<String, String> transformRow(FieldValueList row) {
    return new Pair(
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
      return "s";
    }
    return this.cache.get(id);
  }
}
