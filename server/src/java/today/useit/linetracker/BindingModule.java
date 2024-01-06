package today.useit.linetracker;

import com.github.padster.guiceserver.BaseBindingModule;
import com.github.padster.guiceserver.auth.AppAuthenticator;

import today.useit.linetracker.handlers.*;
import today.useit.linetracker.handlers.data.*;
import today.useit.linetracker.Annotations.ClientPath;
import today.useit.linetracker.auth.AuthenticatorImpl;

import com.google.inject.util.Providers;

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

    bind(AppAuthenticator.class).to(AuthenticatorImpl.class);

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
    // NOTE: nginx only forwards data handlers.
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

    bindDataHandler("/handle_auth", AuthHandler.class);
  }
}
