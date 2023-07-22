/****

Still remaining:
 * Clean up this file, moving each type into its own file.
 * Pre-load ID -> single/compos mapping, hook up into child meta loaders
 * Add settings loader
 * Thread through uid to each section
 * Memory check loading everything at once
 * Switch to write mode against in-memory DB
 * run!

*****/


package today.useit.linetracker.db.transforms;

import today.useit.linetracker.model.*;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.Job;
import com.google.cloud.bigquery.JobId;
import com.google.cloud.bigquery.JobInfo;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;

import com.github.padster.guiceserver.json.JsonParserImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.google.inject.util.Providers;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import javafx.util.Pair;

public class ToSingleLine {

  public List<String> extractIds(String json) {
    Gson gson = new Gson();
    Type type = new TypeToken<List<Map<String, String>>>(){}.getType();
    List<Map<String, String>> childMeta = gson.fromJson(json, type);
    return childMeta.stream().map(c -> c.get("id")).collect(Collectors.toList());
  }

  public String getId(FieldValueList row) {
    return row.get("__key__").getRecordValue().get("name").getStringValue();
  }

  public GraphsLineMeta transformGraph(FieldValueList row) {
    String id = this.getId(row);
    String name = row.get("name").getStringValue();
    // TODO
    // String uid = row.get("uid").getStringValue();
    String childMeta = (String) row.get("childMetadata").getValue();
    List<String> childIds = this.extractIds(childMeta);
    List<String> types = childIds.stream().map(childId -> "s").collect(Collectors.toList());
    List<ChildEntry> children = new ArrayList<ChildEntry>();
    for (int i = 0; i < childIds.size(); i++) {
      children.add(new ChildEntry(childIds.get(i), types.get(i)));
    }

    JsonParserImpl parser = new JsonParserImpl<GraphsLineMeta>(
      Providers.of(new Gson()), new TypeToken<GraphsLineMeta>(){}.getType());

    GraphsLineMeta result = new GraphsLineMeta();
    result.setId(id);
    result.setName(name);
    result.setChildren(children);
    System.out.println("Parsed: " + parser.toJson(result));
    return result;
  }

  public ComposLineMeta transformCompositeLine(FieldValueList row) {
    String id = this.getId(row);
    String name = (String) row.get("name").getValue();
    String link = (String) row.get("link").getValue();
    String op = (String) row.get("op").getValue();
    // TODO
    // String uid = row.get("uid").getStringValue();
    String childMeta = (String) row.get("childMetadata").getValue();

    List<String> childIds = this.extractIds(childMeta);
    List<String> types = childIds.stream().map(childId -> "s").collect(Collectors.toList());
    List<ChildEntry> children = new ArrayList<ChildEntry>();
    for (int i = 0; i < childIds.size(); i++) {
      children.add(new ChildEntry(childIds.get(i), types.get(i)));
    }

    JsonParserImpl parser = new JsonParserImpl<ComposLineMeta>(
      Providers.of(new Gson()), new TypeToken<ComposLineMeta>(){}.getType());

    ComposLineMeta result = new ComposLineMeta();
    result.setId(id);
    result.setName(name);
    result.setOp(op);
    result.setChildren(children);
    System.out.println("Parsed: " + parser.toJson(result));
    return result;

  }

  public SingleLineMeta transformSingleLine(FieldValueList row) {
    String id = this.getId(row);
    String name = (String) row.get("name").getValue();
    String link = (String) row.get("link").getValue();
    // TODO
    // String uid = row.get("uid").getStringValue();

    JsonParserImpl parser = new JsonParserImpl<SingleLineMeta>(
      Providers.of(new Gson()), new TypeToken<SingleLineMeta>(){}.getType());

    SingleLineMeta result = new SingleLineMeta();
    result.setId(id);
    result.setName(name);
    result.setLink(link);
    System.out.println("Parsed: " + parser.toJson(result));
    return result;
  }

  public Pair<String, DatedValue> transformValue(FieldValueList row) {
    double value = row.get("v").getDoubleValue();
    String path = (String) row.get("__key__").getRecordValue().get("path").getValue();
    String[] parts = path.split(", ");
    System.out.println(parts);
    String parent = parts[1].substring(1, parts[1].length() - 1);
    String timestamp = parts[3];

    String pattern = "ddMMyyyy";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    String dayString = simpleDateFormat.format(new Date(Long.parseLong(timestamp)));

    // TODO
    // String uid = row.get("uid").getStringValue();

    JsonParserImpl parser = new JsonParserImpl<DatedValue>(
      Providers.of(new Gson()), new TypeToken<DatedValue>(){}.getType());

    DatedValue result = new DatedValue(dayString, value);
    System.out.println("Parsed: " + parent + " -> " + parser.toJson(result));
    return new Pair(parent, result);
  }

  public List<SingleLineMeta> loadSingleLines() {
    try {
      BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();

      QueryJobConfiguration queryConfig =
          QueryJobConfiguration.newBuilder(
                  "SELECT uid, name, link, __key__ " +
                  "FROM `linetracking.datastore_backup.20221601_pertype_l` " +
                  "WHERE op IS NULL " +
                  "LIMIT 10")
              // Use standard SQL syntax for queries.
              // See: https://cloud.google.com/bigquery/sql-reference/
              .setUseLegacySql(false)
              .build();

      // Create a job ID so that we can safely retry.
      JobId jobId = JobId.of(UUID.randomUUID().toString());
      Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());

      // Wait for the query to complete.
      queryJob = queryJob.waitFor();

      // Check for errors
      if (queryJob == null) {
        throw new RuntimeException("Job no longer exists");
      } else if (queryJob.getStatus().getError() != null) {
        // You can also look at queryJob.getStatus().getExecutionErrors() for all
        // errors, not just the latest one.
        throw new RuntimeException(queryJob.getStatus().getError().toString());
      }

      // Get the results.
      TableResult result = queryJob.getQueryResults();

      // Print all pages of the results.
      List<SingleLineMeta> graphs = new ArrayList<SingleLineMeta>();
      for (FieldValueList row : result.iterateAll()) {
        graphs.add(this.transformSingleLine(row));
      }
      return graphs;
    } catch (Exception e) {
      System.err.println(e);
      return new ArrayList<SingleLineMeta>();
    }
  }

  public List<ComposLineMeta> loadCompositeLines() {
    try {
      BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();

      QueryJobConfiguration queryConfig =
          QueryJobConfiguration.newBuilder(
                  "SELECT op, childMetadata, uid, name, link, __key__ " +
                  "FROM `linetracking.datastore_backup.20221601_pertype_l` " +
                  "WHERE op IS NOT NULL " +
                  "LIMIT 10")
              // Use standard SQL syntax for queries.
              // See: https://cloud.google.com/bigquery/sql-reference/
              .setUseLegacySql(false)
              .build();

      // Create a job ID so that we can safely retry.
      JobId jobId = JobId.of(UUID.randomUUID().toString());
      Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());

      // Wait for the query to complete.
      queryJob = queryJob.waitFor();

      // Check for errors
      if (queryJob == null) {
        throw new RuntimeException("Job no longer exists");
      } else if (queryJob.getStatus().getError() != null) {
        // You can also look at queryJob.getStatus().getExecutionErrors() for all
        // errors, not just the latest one.
        throw new RuntimeException(queryJob.getStatus().getError().toString());
      }

      // Get the results.
      TableResult result = queryJob.getQueryResults();

      // Print all pages of the results.
      List<ComposLineMeta> graphs = new ArrayList<ComposLineMeta>();
      for (FieldValueList row : result.iterateAll()) {
        graphs.add(this.transformCompositeLine(row));
      }
      return graphs;
    } catch (Exception e) {
      System.err.println(e);
      return new ArrayList<ComposLineMeta>();
    }
  }

  // HACK - move out
  public List<GraphsLineMeta> loadGraphs() {
    try {
      BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();

      QueryJobConfiguration queryConfig =
          QueryJobConfiguration.newBuilder(
                  "SELECT childMetadata, uid, name, __key__ " +
                  "FROM `linetracking.datastore_backup.20221601_pertype_g`" +
                  "LIMIT 10")
              // Use standard SQL syntax for queries.
              // See: https://cloud.google.com/bigquery/sql-reference/
              .setUseLegacySql(false)
              .build();

      // Create a job ID so that we can safely retry.
      JobId jobId = JobId.of(UUID.randomUUID().toString());
      Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());

      // Wait for the query to complete.
      queryJob = queryJob.waitFor();

      // Check for errors
      if (queryJob == null) {
        throw new RuntimeException("Job no longer exists");
      } else if (queryJob.getStatus().getError() != null) {
        // You can also look at queryJob.getStatus().getExecutionErrors() for all
        // errors, not just the latest one.
        throw new RuntimeException(queryJob.getStatus().getError().toString());
      }

      // Get the results.
      TableResult result = queryJob.getQueryResults();

      // Print all pages of the results.
      List<GraphsLineMeta> graphs = new ArrayList<GraphsLineMeta>();
      for (FieldValueList row : result.iterateAll()) {
        graphs.add(this.transformGraph(row));
      }
      return graphs;
    } catch (Exception e) {
      System.err.println(e);
      return new ArrayList<GraphsLineMeta>();
    }
  }

  // HACK - move out
  public List<Pair<String, DatedValue>> loadValues() {
    try {
      System.out.println("Here...");
      BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();

      QueryJobConfiguration queryConfig =
          QueryJobConfiguration.newBuilder(
                  "SELECT v, __key__ " +
                  "FROM `linetracking.datastore_backup.20221601_pertype_v`" +
                  "LIMIT 10")
              // Use standard SQL syntax for queries.
              // See: https://cloud.google.com/bigquery/sql-reference/
              .setUseLegacySql(false)
              .build();

      // Create a job ID so that we can safely retry.
      JobId jobId = JobId.of(UUID.randomUUID().toString());
      Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());

      // Wait for the query to complete.
      queryJob = queryJob.waitFor();

      // Check for errors
      if (queryJob == null) {
        throw new RuntimeException("Job no longer exists");
      } else if (queryJob.getStatus().getError() != null) {
        // You can also look at queryJob.getStatus().getExecutionErrors() for all
        // errors, not just the latest one.
        throw new RuntimeException(queryJob.getStatus().getError().toString());
      }

      // Get the results.
      TableResult result = queryJob.getQueryResults();

      // Print all pages of the results.
      List<Pair<String, DatedValue>> values = new ArrayList<Pair<String, DatedValue>>();
      for (FieldValueList row : result.iterateAll()) {
        values.add(this.transformValue(row));
      }
      return values;
    } catch (Exception e) {
      e.printStackTrace();
      return new ArrayList<Pair<String, DatedValue>>();
    }
  }
}
