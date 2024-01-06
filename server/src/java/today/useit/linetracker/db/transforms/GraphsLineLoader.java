package today.useit.linetracker.db.transforms;

import today.useit.linetracker.model.*;
import java.util.List;
import com.google.cloud.bigquery.FieldValueList;
import com.google.gson.Gson;

public class GraphsLineLoader extends BaseLoader<GraphsLineMeta> {
  private final LineTypeLoader lineTypes;

  public GraphsLineLoader(Gson gson, String uid, LineTypeLoader lineTypes) {
    super(gson, uid);
    this.lineTypes = lineTypes;
  }

  public String getQuery() {
    return
      "SELECT __key__, uid, name, childMetadata \n" +
      "FROM `linetracking.datastore_backup.20221601_pertype_g` \n" +
      "WHERE uid = \"" + this.uid + "\"";
  }

  public GraphsLineMeta transformRow(FieldValueList row) {
    String id = this.getId(row);
    // String uid = row.get("uid").getStringValue(); // TODO
    String name = row.get("name").getStringValue();
    List<ChildEntry> children = this.extractChildren(lineTypes, row.get("childMetadata").getStringValue());

    GraphsLineMeta result = new GraphsLineMeta();
    result.setId(id);
    result.setName(name);
    result.setChildren(children);
    return result;
  }
}
