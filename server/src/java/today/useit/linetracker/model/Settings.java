package today.useit.linetracker.model;

import java.util.List;

public class Settings {
  public final String homeID;

  public Settings(String homeID) {
    this.homeID = homeID;
  }

  @Override public int hashCode() {
    return homeID.hashCode();
  }

  @Override public boolean equals(Object other) {
    return (other instanceof Settings)
      ? homeID.equals(((Settings)other).homeID)
      : false;
  }
}
