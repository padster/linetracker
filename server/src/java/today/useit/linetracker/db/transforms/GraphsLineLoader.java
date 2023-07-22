package today.useit.linetracker.db.transforms;

import today.useit.linetracker.model.*;
import today.useit.linetracker.db.transforms.BaseLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.cloud.bigquery.FieldValueList;
import com.google.gson.Gson;

public class GraphsLineLoader extends BaseLoader<GraphsLineMeta> {
  private final LineTypeLoader lineTypes;

  public GraphsLineLoader(Gson gson, LineTypeLoader lineTypes) {
    super(gson);
    this.lineTypes = lineTypes;
  }

  public String getQuery() {
    return
      "SELECT __key__, uid, name, childMetadata \n" +
      "FROM `linetracking.datastore_backup.20221601_pertype_g`";
  }

  public GraphsLineMeta transformRow(FieldValueList row) {
    String id = this.getId(row);
    // String uid = row.get("uid").getStringValue(); // TODO
    String name = row.get("name").getStringValue();

    String childMeta = (String) row.get("childMetadata").getValue();
    List<ChildEntry> children = this.extractIds(childMeta).stream()
      .map(childId -> new ChildEntry(childId, this.lineTypes.getTypeForID(childId)))
      .collect(Collectors.toList());

    GraphsLineMeta result = new GraphsLineMeta();
    result.setId(id);
    result.setName(name);
    result.setChildren(children);
    return result;
  }
}