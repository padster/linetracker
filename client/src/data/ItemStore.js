/**
 * Holds all the data about a given item type within the app.
 * PICK: replace with Treevent object?
 */
class ItemStore {
  // Root path for handlers serving the data.
  serverBase: String;
  idPrefix: String;

  listeners: Map;

  // Map id -> Metadata about the objects
  items: Map;

  // List of all items loaded. PICK: merge with above?
  itemList: Array<Object>;

  // Map id -> Timeseries datevalues for each item.
  // TODO

  constructor(serverBase: String, idPrefix: String) {
    this.serverBase = serverBase;
    this.idPrefix = idPrefix;
    this.listeners = new Map();
    this.items = new Map();
    this.itemList = undefined;

    // Hmm, odd.
    window.$.ajaxSetup({
      scriptCharset: "utf-8",
      contentType: "application/json; charset=utf-8",
      xhrFields: {
        withCredentials: true
      },
    });
  }

  addListener(path: string, listener: Function): Function {
    console.log("Listening for " + path);
    if (!this.listeners.has(path)) {
      this.listeners.set(path, new Set());
    }
    if (this.listeners.get(path).has(listener)) {
      console.error("Adding listener twice! :(");
      return null;
    }
    this.listeners.get(path).add(listener);
    return () => this.listeners.get(path).delete(listener);
  }

  get(id: string): ?Object {
    if (this.items.has(id)) {
      return this.items.get(id);
    }
    this.items.set(id, undefined); // HACK - use loading object.
    window.$.getJSON(`${this.serverBase}/${id}`)
      .done(data => {
        this._insertFullID(data);
        this.items.set(id, data);
        this._triggerListeners([id]);
      })
      .fail(() => console.error("Couldn't load"));
  }

  list(): Array<Object> {
    if (this.itemList !== undefined) {
      return this.itemList;
    }
    window.$.getJSON(`${this.serverBase}`)
      .done(data => {
        this.itemList = data;
        this.itemList.forEach(this._insertFullID.bind(this))
        // NOTE: listeners for children not triggered
        this._triggerListeners([]);
      })
      .fail(() => console.error("Couldn't load"));
  }

  create(meta: Object, callback: Function) {
    window.$.ajax({
      type: "POST",
      url: `${this.serverBase}`,
      dataType: 'json',
      data: JSON.stringify(meta),
    }).done(data => {
      callback(data);
    }).fail(() => {
      alert("Oops, cant create...");
    });
  }

  delete(id: String) {
    window.$.ajax({
      type: "DELETE",
      url: `${this.serverBase}/${id}`,
      dataType: 'json'
    }).done(data => {
      // HACK - improve.
      this.items.delete(id);
      this.itemList = undefined;
      this._triggerListeners([id]);
    }).fail(() => {
      alert("Oops, cant delete...");
    });
  }

  clearCacheForItem(id: String) {
    this.items.delete(id);
    this.itemList = undefined;
    this._triggerListeners([id]);
  }

  // HACK - remove
  triggerListeners(path) { this._triggerListeners(path); }

  _triggerListeners(path) {
    for (let i = path.length; i >= 0; i--) {
      const listenerPath = path.slice(0, i).join('/');
      console.log("trigger listener at " + listenerPath);
      const listeners = this.listeners.get(listenerPath) || [];
      listeners.forEach(listener => listener());
    }
  }

  _insertFullID(line: Object) {
    line.fullID = `${this.idPrefix}/${line.id}`;
  }
}

export default ItemStore;
