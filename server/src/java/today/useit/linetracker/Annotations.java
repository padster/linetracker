package today.useit.linetracker;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import com.google.inject.BindingAnnotation;

public final class Annotations {
  private Annotations() {}

  @Retention(RetentionPolicy.RUNTIME)
  @BindingAnnotation
  public @interface ClientPath {}
}
