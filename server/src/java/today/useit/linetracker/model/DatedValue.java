package today.useit.linetracker.model;

public class DatedValue implements Comparable<DatedValue> {
  // Date this is for, in the form YYYYMMDD
  public String t;
  // Value.
  public double v;

  public DatedValue() {}

  public DatedValue(String time, double value) {
    this.t = time;
    this.v = value;
  }

  @Override public int compareTo(DatedValue other) {
    return t.compareTo(other.t);
  }

  @Override public int hashCode() {
    return this.t.hashCode();
  }

  @Override public boolean equals(Object other) {
    return (other instanceof DatedValue)
      ? this.t.equals(((DatedValue)other).t)
      : false;
  }
}
