package today.useit.linetracker;

import com.github.padster.guiceserver.BaseBindingModule;
import today.useit.linetracker.handlers.*;
import today.useit.linetracker.handlers.data.*;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

import javax.inject.Provider;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides all the path -> handler bindings, and a way for other modules to configure them.
 */
public class BindingModule extends BaseBindingModule {
  @Override protected void bindPageHandlers() {
    // TODO
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
