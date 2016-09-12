import ItemStore from './ItemStore.js';

/**
 * Applys modification to the children of lines.
 * *DOES NOT* cache any data itself.
 */
class ChildStore {
  // Root path for handlers serving the data.
  serverBase: String;

  composStore: ItemStore;
  graphsStore: ItemStore;

  constructor(serverBase: String, composStore: ItemStore, graphsStore: ItemStore) {
    this.serverBase = serverBase;
    this.composStore = composStore;
    this.graphsStore = graphsStore;
  }

  addChildren(fullID: String, children: Array<Object>) {
    const childParts = fullID.split('/');

    // EditChildrenRequest
    const data = {toAdd: children};
    window.$.ajax({
      type: "POST",
      url: `${this.serverBase}/${fullID}/children`,
      dataType: 'json',
      data: JSON.stringify(data),
    }).done(data => {
      console.log("Added!");
      if (childParts[0] === 'compos') {
        this.composStore.clearCacheForItem(childParts[1]);
      } else if (childParts[0] === 'graphs') {
        this.graphsStore.clearCacheForItem(childParts[1]);
      } else {
        console.log("oops, invalid type for children: " + childParts[0]);
      }
    }).fail(() => {
      alert("Oops, cant create...");
    });
  }

  removeChild(fullID: String, child: Object) {
    const childParts = fullID.split('/');

    // EditChildrenRequest
    const data = {toRemove: child};
    window.$.ajax({
      type: "POST",
      url: `${this.serverBase}/${fullID}/children`,
      dataType: 'json',
      data: JSON.stringify(data),
    }).done(data => {
      console.log("Removed!");
      if (childParts[0] === 'compos') {
        this.composStore.clearCacheForItem(childParts[1]);
      } else if (childParts[0] === 'graphs') {
        this.graphsStore.clearCacheForItem(childParts[1]);
      } else {
        console.log("oops, invalid type for children: " + childParts[0]);
      }
    }).fail(() => {
      alert("Oops, cant create...");
    });
  }
}

export default ChildStore;
