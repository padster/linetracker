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

  // PICK: merge with the above?
  singleList: Array<Object>;
  composList: Array<Object>;
  graphsList: Array<Object>;

  // Map id -> Timeseries datevalues for each item.
  // TODO

  constructor() {
    this.listeners = new Map();

    this.singleItems = new Map();
    this.composItems = new Map();
    this.graphsItems = new Map();

    this.singleList = undefined;
    this.composList = undefined;
    this.graphsList = undefined;

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

  listSingle(): Array<Object> {
    if (this.singleList !== undefined) {
      return this.singleList;
    }
    window.$.getJSON(`${SERVER_PREFIX}/_/single`)
      .done(data => {
        this.singleList = data;
        // NOTE: listeners for children not triggered
        this._triggerListeners(['single']);
      })
      .fail(() => console.error("Couldn't load"));
  }

  listCompos(): Array<Object> {
    if (this.composList !== undefined) {
      return this.composList;
    }
    window.$.getJSON(`${SERVER_PREFIX}/_/compos`)
      .done(data => {
        this.composList = data;
        // NOTE: listeners for children not triggered
        this._triggerListeners(['compos']);
      })
      .fail(() => console.error("Couldn't load"));
  }

  listGraphs(): Array<Object> {
    if (this.graphsList !== undefined) {
      return this.graphsList;
    }
    window.$.getJSON(`${SERVER_PREFIX}/_/graphs`)
      .done(data => {
        this.graphsList = data;
        // NOTE: listeners for children not triggered
        this._triggerListeners(['graphs']);
      })
      .fail(() => console.error("Couldn't load"));
  }

  deleteSingle(id: string, callback: Function) {
    window.$.ajax({
      type: "DELETE",
      url: `${SERVER_PREFIX}/_/single/${id}`,
      dataType: 'json'
    }).done(data => {
      alert("Deleted: recv = " + window.JSON.stringify(data));
      // HACK - improve.
      this.singleItems.delete(id);
      this.singleList = undefined;
      callback();
    }).fail(() => {
      alert("Oops, cant delete...");
    });
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
