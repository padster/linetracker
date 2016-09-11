/**
 * Holds all the dated values within a line.
 * PICK: replace with Treevent object?
 */
class ValuesStore {
  // Root path for handlers serving the data.
  serverBase: String;

  listeners: Map;

  // Map full id -> list of dated values for each line
  values: Map;

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

  get(fullID: String): ?List<Object> {
    const path = this._fullIDToPath(fullID);
    if (this.values.has(fullID)) {
      return this.values.get(fullID);
    }
    this.values.set(fullID, undefined); // HACK - use loading object.
    window.$.getJSON(`${this.serverBase}/${fullID}`)
      .done(data => {
        this.values.set(fullID, data);
        this._triggerListeners(path);
      })
      .fail(() => console.error("Couldn't load"));
  }

  insert(fullID: String, values: List<Object>, callback: Function) {
    const path = this._fullIDToPath(fullID);
    if (path[0] !== 'single') {
      alert("oops. Should only insert values in basic lines.");
      return;
    }
    window.$.ajax({
      type: "POST",
      url: `${this.serverBase}/${fullID}`,
      dataType: 'json',
      data: JSON.stringify(values),
    }).done(data => {
      callback(data);
    }).fail(() => {
      alert("Oops, cant create...");
    });
  }

  delete(fullID: String, dateYYYYMMDD: String) {
    const path = this._fullIDToPath(fullID);
    if (path[0] !== 'single') {
      alert("oops. Should only insert values in basic lines.");
      return;
    }
    window.$.ajax({
      type: "DELETE",
      url: `${this.serverBase}/${fullID}/${dateYYYYMMDD}`,
      dataType: 'json'
    }).done(data => {
      // HACK - improve.
      this.values.delete(fullID);
      // this._triggerListeners([id]);
    }).fail(() => {
      alert("Oops, cant delete...");
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

  _fullIDToPath(fullID: String): Array<String> {
    const path = fullID.split('/');
    if (path.length !== 2 || !(path[0] === 'single' || path[1] === 'compos')) {
      alert("Whoops! Value full ID wrong, This shouldn't happen...");
    }
    return path;
  }
}

export default ValuesStore;
