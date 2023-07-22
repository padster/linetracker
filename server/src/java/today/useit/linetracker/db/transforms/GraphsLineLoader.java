package today.useit.linetracker.db.transforms;

import today.useit.linetracker.model.*;
import today.useit.linetracker.db.transforms.BaseLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.cloud.bigquery.FieldValueList;
import com.google.gson.Gson;

public class GraphsLineLoader extends BaseLoader<GraphsLineMeta> {

  public GraphsLineLoader(Gson gson) {
    super(gson);
  }

  public String getQuery() {
    return
      "SELECT __key__, uid, name, childMetadata \n" +
      "FROM `linetracking.datastore_backup.20221601_pertype_g` \n" +
      "LIMIT 10";
  }

  public GraphsLineMeta transformRow(FieldValueList row) {
    String id = this.getId(row);
    // String uid = row.get("uid").getStringValue(); // TODO
    String name = row.get("name").getStringValue();

    String childMeta = (String) row.get("childMetadata").getValue();
    List<String> childIds = this.extractIds(childMeta);
    List<String> types = childIds.stream().map(childId -> "s").collect(Collectors.toList());
    List<ChildEntry> children = new ArrayList<ChildEntry>();
    for (int i = 0; i < childIds.size(); i++) {
      children.add(new ChildEntry(childIds.get(i), types.get(i)));
    }

    GraphsLineMeta result = new GraphsLineMeta();
    result.setId(id);
    result.setName(name);
    result.setChildren(children);

    // HACK
    System.out.println("Parsed: " + this.fmt(result));
    return result;
  }
}
