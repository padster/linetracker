// HACK - remove before deploy, pass server location from server.
const SERVER_DATA_PREFIX = 'http://localhost:8080/_';

import ItemStore from './ItemStore.js';
import SingleStore from './SingleStore.js';

/** Holds all the data within the app. */
const Stores = {
  singleStore: new SingleStore(`${SERVER_DATA_PREFIX}/single`),
  composStore: new ItemStore(`${SERVER_DATA_PREFIX}/compos`),
  graphsStore: new ItemStore(`${SERVER_DATA_PREFIX}/graphs`),
};

export default Stores;
