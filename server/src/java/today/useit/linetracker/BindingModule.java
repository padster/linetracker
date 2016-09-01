package today.useit.linetracker;

import today.useit.linetracker.handlers.*;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.TypeLiteral;

import javax.inject.Provider;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides all the path -> handler bindings, and a way for other modules to configure them.
 */
public class BindingModule extends AbstractModule {
  /** Handler Bindings for this server. */
  void defaultBindings() {
    bindHandler("/", HelloHandler.class);
  }

  @Retention(RetentionPolicy.RUNTIME)
  @BindingAnnotation
  public @interface Bindings {}

  @Retention(RetentionPolicy.RUNTIME)
  @BindingAnnotation
  public @interface ServerPort {}

  @Retention(RetentionPolicy.RUNTIME)
  @BindingAnnotation
  public @interface BackupFilePath {}

  public final Map<String, Provider<? extends Action>> bindings = new HashMap<>();

  @Override protected void configure() {
    bind(new TypeLiteral<Map<String, Provider<? extends Action>>>(){})
        .annotatedWith(Bindings.class)
        .toInstance(bindings);

    defaultBindings();

    bind(MustacheFactory.class).toInstance(new DefaultMustacheFactory());
  }

  public <T extends Action> void bindHandler(String path, Class<T> handlerClass) {
    bind(handlerClass);
    bindings.put(path, this.getProvider(handlerClass));
  }
}
