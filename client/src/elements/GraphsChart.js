import React, { Component } from 'react';

import LineChart from './LineChart.js';
import LoadingIndicator from './LoadingIndicator.js';

import Stores from '../data/Stores.js';

class GraphsChart extends Component {
  disposeItem: null;
  disposeValues: null;
  disposeSingleList: null;
  disposeComposList: null;

  componentWillMount() {
    // TODO - change dispose & re-listen on id change.
    this.disposeItem = Stores.graphsStore.addListener(`${this.props.id}`, () => {
      console.log("Item changed!");
      this.forceUpdate();
    });
    this.disposeValues = Stores.valuesStore.addListener(``, () => {
      console.log("Values changed!");
      this.forceUpdate();
    });
    this.disposeSingleList = Stores.singleStore.addListener('', () => {
      console.log("Single list updated!");
      this.forceUpdate();
    });
    this.disposeComposList = Stores.composStore.addListener('', () => {
      console.log("Compos list updated!");
      this.forceUpdate();
    });
  }

  componentWillUnmount() {
    this.disposeComposList();
    this.disposeSingleList();
    this.disposeValues();
    this.disposeItem();
  }

  render() {
    console.log("LOADING");
    const line = Stores.graphsStore.get(this.props.id);
    if (line === undefined) {
      return <LoadingIndicator />;
    }

    console.log(line);

    const childrenWithNames = Stores.lookUpChildNames(line.childMetadata);
    if (childrenWithNames === undefined) {
      return <LoadingIndicator />;
    }

    console.log(childrenWithNames);

    let allChildValues = true;
    const childLines = childrenWithNames.map(child => {
      const childValues = Stores.valuesStore.get(`${child.type}/${child.id}`);
      if (childValues === undefined || childValues === null) {
        allChildValues = false;
      }
      return {name: child.name, values: childValues};
    });

    if (!allChildValues) {
      return <LoadingIndicator />;
    }

    return <LineChart lines={childLines}/>;
  }
}

export default GraphsChart;
