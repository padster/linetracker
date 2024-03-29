// HACK - remove before deploy, pass server location from server.
import ChildStore from './ChildStore.js';
import ItemStore from './ItemStore.js';
import SettingsStore from './SettingsStore.js';
import ValuesStore from './ValuesStore.js';

const SERVER_DATA_PREFIX = process.env.REACT_APP_SERVER ? `${process.env.REACT_APP_SERVER}/_` : '/_';

/** Holds all the data within the app. */
const Stores = {
  singleStore:       new ItemStore(`${SERVER_DATA_PREFIX}/single`, 'single'),
  composStore:       new ItemStore(`${SERVER_DATA_PREFIX}/compos`, 'compos'),
  graphsStore:       new ItemStore(`${SERVER_DATA_PREFIX}/graphs`, 'graphs'),
  valuesStore:     new ValuesStore(`${SERVER_DATA_PREFIX}/values`),
  settingsStore: new SettingsStore(`${SERVER_DATA_PREFIX}/settings`),

  // MEGA HACK - assumes compos
  lookUpChildNames(childIds: Array<Object>): Array<Object> {
    const allSingle = this.singleStore.list();
    const allCompos = this.composStore.list();
    if (allSingle === undefined || allCompos === undefined) {
      return undefined;
    }
    return childIds.map(child => {
      let name = undefined;
      if (child.type === "single") {
        const line = allSingle.find(s => s.id === child.id);
        name = line === undefined ? '???' : line.name;
      } else if (child.type === "compos") {
        const line = allCompos.find(s => s.id === child.id);
        name = line === undefined ? '???' : line.name;
      } else if (child.type === "const") {
        name = `Constant (${child.value})`
      }
      return {...child, name};
    });
  }
};

Stores.childStore = new ChildStore(SERVER_DATA_PREFIX, Stores.composStore, Stores.graphsStore);

export default Stores;
