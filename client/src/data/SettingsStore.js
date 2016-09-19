/**
 * Holds all the dated values within a line.
 * PICK: replace with Treevent object?
 */
class SettingsStore {
  // Root path for handlers serving the data.
  serverBase: String;

  listeners: Map;

  settings: Object;

  constructor(serverBase: String) {
    this.serverBase = serverBase;
    this.listeners = new Map();
    this.values = new Map();
  }

  addListener(path: string, listener: Function): Function {
    if (!this.listeners.has(path)) {
      this.listeners.set(path, new Set());
    }
    if (this.listeners.get(path).has(listener)) {
      console.error("Adding listener twice! :(");
      return null;
    }
    this.listeners.get(path).add(listener);
    return () => this.listeners.get(path).remove(listener);
  }

  get(): Object {
    if (this.settings !== undefined) {
      return this.settings;
    }
    console.log("GETTING SETTINGS");
    window.$.getJSON(`${this.serverBase}`)
      .done(data => {
        console.log("GOT! %O", data);
        this.settings = data;
        this._triggerListeners([]);
      })
      .fail(() => console.error("Couldn't load"));
  }

  update(settings: Object) {
    this.settings = settings;
    this._triggerListeners([]);
    window.$.ajax({
      type: "POST",
      url: `${this.serverBase}`,
      dataType: 'json',
      data: JSON.stringify(settings),
    }).done(data => {
      this.settings = data;
      this._triggerListeners([]);
    }).fail(() => {
      alert("Oops, cant create...");
    });
  }

  _triggerListeners(path) {
    for (let i = path.length; i >= 0; i--) {
      const listenerPath = path.slice(0, i).join('/');
      console.log("trigger listener at " + listenerPath);
      const listeners = this.listeners.get(listenerPath) || [];
      listeners.forEach(listener => listener());
    }
  }
}

export default SettingsStore;
