// HACK - remove before deploy, pass server location from server.
const SERVER_PREFIX = 'http://localhost:8080';

/**
 * Holds all the data within the app.
 * PICK: replace with Treevent object?
 */
class StoreImpl {
  listeners: Map;

  // Map id -> Metadata about the objects
  singleItems: Map;
  composItems: Map;
  graphsItems: Map;

  // Map id -> Timeseries datevalues for each item.
  // TODO

  constructor() {
    this.listeners = new Map();

    this.singleItems = new Map();
    this.composItems = new Map();
    this.graphsItems = new Map();

    // Hmm, odd.
    window.$.ajaxSetup({
      scriptCharset: "utf-8",
      contentType: "application/json; charset=utf-8"
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
    return () => this.listeners.get(path).remove(listener);
  }

  getSingle(id: string): ?Object {
    if (this.singleItems.has(id)) {
      return this.singleItems.get(id);
    }
    this.singleItems.set(id, undefined); // HACK - use loading object.
    window.$.getJSON(`${SERVER_PREFIX}/_/single/${id}`)
      .done(data => {
        this.singleItems.set(id, data);
        this._triggerListeners(['single', id]);
      })
      .fail(() => console.error("Couldn't load"));
  }

  getCompos(id: string): ?Object {
    if (this.composItems.has(id)) {
      return this.composItems.get(id);
    }
    this.composItems.set(id, undefined); // HACK - use loading object.
    window.$.getJSON(`${SERVER_PREFIX}/_/compos/${id}`)
      .done(data => {
        this.composItems.set(id, data);
        this._triggerListeners(['compos', id]);
      })
      .fail(() => console.error("Couldn't load"));
  }

  getGraphs(id: string): ?Object {
    if (this.graphsItems.has(id)) {
      return this.graphsItems.get(id);
    }
    this.graphsItems.set(id, undefined); // HACK - use loading object.
    window.$.getJSON(`${SERVER_PREFIX}/_/graphs/${id}`)
      .done(data => {
        this.graphsItems.set(id, data);
        this._triggerListeners(['graphs', id]);
      })
      .fail(() => console.error("Couldn't load"));
  }

  _triggerListeners(path) {
    for (let i = path.length; i > 0; i--) {
      const listenerPath = path.slice(0, i).join('/');
      console.log("trigger listener at " + listenerPath);
      const listeners = this.listeners.get(listenerPath) || [];
      listeners.forEach(listener => listener());
    }
  }
}

const Store = new StoreImpl();

export default Store;
