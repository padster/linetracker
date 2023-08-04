package today.useit.linetracker.model;

/** Operations allowed on composite lines. */
public enum CompositeOperation {
  PLUS(0) { // a + b + c + ...
    @Override public double reduce(double collector, double next) {
      return collector + next;
    }
  },
  TIMES(1) { // a * b * c * ...
    @Override public double reduce(double collector, double next) {
      return collector * next;
    }
  },
  // Inverses of the above!
  NEGATE(0) { // 0 - PLUS(a, b, c, ...)
    @Override public double reduce(double collector, double next) {
      return collector - next;
    }
  },
  INVMULT(1) { // 1 / TIMES(a, b, c, ...)
    @Override public double reduce(double collector, double next) {
      if (next == 0) {
        next = 1e-12; // Avoid zero division, although the result is still meaningless probably.
      }
      return collector / next;
    }
  };

  // Serialized form.
  private final double identity;

  private CompositeOperation(double identity) {
    this.identity = identity;
  }

  public abstract double reduce(double collector, double next);

  public double identity() {
    return this.identity;
  }

  public static CompositeOperation fromText(String text) {
    switch(text.toUpperCase()) {
      case "PLUS": return CompositeOperation.PLUS;
      case "TIMES": return CompositeOperation.TIMES;
      case "NEGATE": return CompositeOperation.NEGATE;
      case "INVMULT": return CompositeOperation.INVMULT;
    }
    throw new IllegalArgumentException("Invalid operation: \"" + text + "\"");
  }
}
