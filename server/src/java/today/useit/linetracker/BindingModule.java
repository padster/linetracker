package today.useit.linetracker;

import com.github.padster.guiceserver.BaseBindingModule;
import today.useit.linetracker.handlers.*;
import today.useit.linetracker.handlers.data.*;
import today.useit.linetracker.Annotations.ClientPath;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Providers;

import javax.inject.Provider;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides all the path -> handler bindings, and a way for other modules to configure them.
 */
public class BindingModule extends BaseBindingModule {
  public final String clientPath;

  public BindingModule(String clientPath) {
    this.clientPath = clientPath;
  }

  @Override protected void configure() {
    super.configure();

    if (this.clientPath != null) {
      bind(String.class).annotatedWith(ClientPath.class).toInstance(this.clientPath);
    } else {
      bind(String.class).annotatedWith(ClientPath.class).toProvider(Providers.of(null));
    }
  }

  @Override protected void bindPageHandlers() {
    if (this.clientPath != null) {
      System.out.println("BINDING STATIC");
      bindPageHandler("/static/**", StaticHandler.class);
    }
  }

  protected void bindDataHandlers() {
    bindDataHandler("/compos", ListComposHandler.class);
    bindDataHandler("/graphs", ListGraphsHandler.class);
    bindDataHandler("/single", ListSingleHandler.class);
    bindDataHandler("/compos/:id", GetComposHandler.class);
    bindDataHandler("/graphs/:id", GetGraphsHandler.class);
    bindDataHandler("/single/:id", GetSingleHandler.class);
    bindDataHandler("/values/:type/:id", ValuesHandler.class);
    bindDataHandler("/values/single/:id/:yyyymmdd", DatedValueHandler.class);
    bindDataHandler("/:type/:id/children", EditChildrenHandler.class);
    bindDataHandler("/settings", SettingsHandler.class);
  }
}
