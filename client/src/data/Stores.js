// HACK - remove before deploy, pass server location from server.
const SERVER_DATA_PREFIX = 'http://localhost:8080/_';

import ItemStore from './ItemStore.js';
// import SingleStore from './SingleStore.js';
import ValuesStore from './ValuesStore.js';

/** Holds all the data within the app. */
const Stores = {
  singleStore: new ItemStore(  `${SERVER_DATA_PREFIX}/single`, 'single'),
  composStore: new ItemStore(  `${SERVER_DATA_PREFIX}/compos`, 'compos'),
  graphsStore: new ItemStore(  `${SERVER_DATA_PREFIX}/graphs`, 'graphs'),
  valuesStore: new ValuesStore(`${SERVER_DATA_PREFIX}/values`),
};

export default Stores;
