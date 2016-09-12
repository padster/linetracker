// HACK - remove before deploy, pass server location from server.
const SERVER_DATA_PREFIX = 'http://localhost:8080/_';

import ChildStore from './ChildStore.js';
import ItemStore from './ItemStore.js';
import ValuesStore from './ValuesStore.js';

/** Holds all the data within the app. */
const Stores = {
  singleStore: new ItemStore(  `${SERVER_DATA_PREFIX}/single`, 'single'),
  composStore: new ItemStore(  `${SERVER_DATA_PREFIX}/compos`, 'compos'),
  graphsStore: new ItemStore(  `${SERVER_DATA_PREFIX}/graphs`, 'graphs'),
  valuesStore: new ValuesStore(`${SERVER_DATA_PREFIX}/values`),

  // MEGA HACK - assumes compos
  lookUpChildNames(childIds: Array<Object>): Array<Object> {
    const allSingle = this.singleStore.list();
    const allCompos = this.composStore.list();
    if (allSingle === undefined || allCompos === undefined) {
      return undefined;
    }
    return childIds.map(child => {
      let line = undefined;
      if (child.type === "single") {
        line = allSingle.find(s => s.id === child.id);
      } else if (child.type === "compos") {
        line = allCompos.find(s => s.id === child.id);
      }
      console.log(line);
      const name = line === undefined ? '???' : line.name;
      return {...child, name};
    });
  }
};

Stores.childStore = new ChildStore(SERVER_DATA_PREFIX, Stores.composStore, Stores.graphsStore);

export default Stores;
