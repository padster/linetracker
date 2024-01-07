package today.useit.linetracker.store;

import today.useit.linetracker.model.ComposLineMeta;
import today.useit.linetracker.model.CompositeOperation;
import today.useit.linetracker.model.DatedValue;
import today.useit.linetracker.model.DateFormat;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class CalculatingValuesStore implements ValuesStore {
  private final SingleLineValuesStore singleListValuesStore;
  private final ItemStore<ComposLineMeta> composStore;
  private final ChildStore childStore;

  // TODO - request scoped, this isn't cleared properly.
  private final Map<String, SortedSet<DatedValue>> composCache = new HashMap<>();

  public CalculatingValuesStore(
    SingleLineValuesStore singleListValuesStore,
    ItemStore<ComposLineMeta> composStore,
    ChildStore childStore
  ) {
    this.singleListValuesStore = singleListValuesStore;
    this.composStore = composStore;
    this.childStore = childStore;
  }

  public List<DatedValue> valuesForSingle(String id) {
    // Pick: or new ArrayList<>(this.valueSetForSingle(id)) ??
    return this.valueSetForSingle(id).stream().collect(Collectors.toList());
  }

  public List<DatedValue> valuesForCompos(String id) {
    return this.valueSetForCompos(id).stream().collect(Collectors.toList());
  }

  public void addValuesToSingleLine(String id, List<DatedValue> values) {
    this.singleListValuesStore.addValues(id, values);
  }

  public boolean removeValueFromSingleLine(String id, String date) {
    return this.singleListValuesStore.removeValue(id, date);
  }

  public boolean removeAllValuesFromSingleLine(String id) {
    return this.singleListValuesStore.removeAllValues(id);
  }

  ///

  private SortedSet<DatedValue> valueSetForSingle(String id) {
    return this.singleListValuesStore.values(id);
  }

  private SortedSet<DatedValue> valueSetForCompos(String id) {
    if (composCache.containsKey(id)) {
      return composCache.get(id);
    }

    ComposLineMeta lineMeta = this.composStore.getItem(id);
    CompositeOperation op = CompositeOperation.fromText(lineMeta.op);

    List<SortedSet<DatedValue>> childData = childStore.getChildren("compos/" + id)
      .stream()
      .map(child -> {
        if ("single".equals(child.type)) {
          return this.valueSetForSingle(child.id);
        } else if ("compos".equals(child.type)) {
          return this.valueSetForCompos(child.id);
        }
        return new TreeSet<DatedValue>();
      })
      .collect(Collectors.toList());

    SortedSet<String> dates = new TreeSet<>();
    for (SortedSet<DatedValue> child : childData) {
      for (DatedValue value : child) {
        dates.add(value.t);
      }
    }

    SortedSet<DatedValue> result = new TreeSet<>();
    for (String date : dates) {
      double value = op.identity();
      for (SortedSet<DatedValue> child : childData) {
        double childValue = interpolateValue(child, date);
        value = op.reduce(value, childValue);
      }
      result.add(new DatedValue(date, value));
    }
    composCache.put(id, result);
    return result;
  }

  private double interpolateValue(SortedSet<DatedValue> values, String date) {
    DatedValue dateAsValue = new DatedValue(date, 0);
    if (values.contains(dateAsValue)) {
      // HACK - probably should be using a map for this...
      return values.tailSet(dateAsValue).first().v;
    }

    if (values.isEmpty() ||
      date.compareTo(values.first().t) < 0 ||
      date.compareTo(values.last().t) > 0
    ) {
      return 0.0; // No extrapolation.
    }

    DatedValue prev = values.headSet(dateAsValue).last();
    DatedValue next = values.tailSet(dateAsValue).first();
    double prevValue = prev.v;
    double nextValue = next.v;
    try {
      double prevMs = DateFormat.dateToMs(prev.t);
      double nextMs = DateFormat.dateToMs(next.t);
      double dateMs = DateFormat.dateToMs(date);
      return prevValue + (nextValue - prevValue) * (dateMs - prevMs) / (nextMs - prevMs);
    } catch (ParseException pe) {
      // Whoops?
      return 0.0;
    }
  }
}
