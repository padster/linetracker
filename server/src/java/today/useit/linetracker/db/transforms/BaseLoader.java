/** TODO: Clean */

package today.useit.linetracker.db.transforms;

import today.useit.linetracker.model.*;

import com.google.cloud.bigquery.*;

import com.github.padster.guiceserver.json.JsonParserImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import com.google.gson.Gson;
import com.google.inject.util.Providers;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public abstract class BaseLoader<T> {
  protected final Gson gson;
  protected final String uid; // Active user

  public BaseLoader(Gson gson, String uid) {
    this.gson = gson;
    this.uid = uid;
  }

  protected abstract T transformRow(FieldValueList row);

  protected abstract String getQuery();

  public List<T> loadAll() {
      try {
        BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();
        QueryJobConfiguration queryConfig = QueryJobConfiguration
          .newBuilder(this.getQuery())
          .setUseLegacySql(false)
          .build();

        // Create a job ID so that we can safely retry.
        JobId jobId = JobId.of(UUID.randomUUID().toString());
        Job queryJob = bigquery.create(
          JobInfo.newBuilder(queryConfig).setJobId(jobId).build()
        ).waitFor();

        // Check for errors
        if (queryJob == null) {
          throw new RuntimeException("Job no longer exists");
        } else if (queryJob.getStatus().getError() != null) {
          throw new RuntimeException(queryJob.getStatus().getError().toString());
        }


        Iterable<FieldValueList> rowStream = queryJob.getQueryResults().iterateAll();
        return StreamSupport
          .stream(rowStream.spliterator(), false)
          .map(r -> this.transformRow(r))
          .collect(Collectors.toList());
      } catch (Exception e) {
        e.printStackTrace();
        return new ArrayList<T>();
      }
  }

  // Utils

  protected List<ChildEntry> extractChildren(LineTypeLoader lineTypes, String json) {
    if (json == null) {
      return new ArrayList<ChildEntry>();
    }

    Type type = new TypeToken<List<Map<String, String>>>(){}.getType();
    List<Map<String, String>> childMeta = this.gson.fromJson(json, type);

    List<ChildEntry> childEntries = new ArrayList<>();
    for (Map<String, String> child : childMeta) {
      if (child.containsKey("id")) {
        String childId = child.get("id");
        String lineType = lineTypes.getTypeForID(childId);
        if (lineType == null) {
          System.out.println("Skipping " + childId + ", deleted?");
        } else {
          childEntries.add(new ChildEntry(lineTypes.getTypeForID(childId), childId));
        }
      } else {
        childEntries.add(new ChildEntry("const", Double.parseDouble(child.get("const"))));
      }
    }
    return childEntries;
  }

  protected String getId(FieldValueList row) {
    return row.get("__key__").getRecordValue().get("name").getStringValue();
  }

  protected String fmt(T result) {
    JsonParserImpl parser = new JsonParserImpl<T>(
      Providers.of(this.gson), new TypeToken<T>(){}.getType());
    return parser.toJson(result);
  }
}
