package today.useit.linetracker.db.transforms;

import today.useit.linetracker.model.*;
import today.useit.linetracker.db.transforms.BaseLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.cloud.bigquery.FieldValueList;
import com.google.gson.Gson;

public class ComposLineLoader extends BaseLoader<ComposLineMeta> {

  public ComposLineLoader(Gson gson) {
    super(gson);
  }

  public String getQuery() {
    return
      "SELECT __key__, uid, name, link, op, childMetadata \n" +
      "FROM `linetracking.datastore_backup.20221601_pertype_l` \n" +
      "WHERE op IS NOT NULL \n" +
      "LIMIT 10";
  }

  public ComposLineMeta transformRow(FieldValueList row) {
    String id = this.getId(row);
    // String uid = row.get("uid").getStringValue(); // TODO
    String name = (String) row.get("name").getValue();
    String link = (String) row.get("link").getValue();
    String op = (String) row.get("op").getValue();
    String childMeta = (String) row.get("childMetadata").getValue();

    List<String> childIds = this.extractIds(childMeta);
    // TODO - proper map
    List<String> types = childIds.stream().map(childId -> "s").collect(Collectors.toList());
    List<ChildEntry> children = new ArrayList<ChildEntry>();
    for (int i = 0; i < childIds.size(); i++) {
      children.add(new ChildEntry(childIds.get(i), types.get(i)));
    }

    ComposLineMeta result = new ComposLineMeta();
    result.setId(id);
    result.setName(name);
    result.setOp(op);
    result.setChildren(children);

    // HACK
    System.out.println("Parsed: " + this.fmt(result));
    return result;
  }
}
