import React, { Component } from 'react';

import LineChart from './LineChart.js';
import LoadingIndicator from './LoadingIndicator.js';

import Stores from '../data/Stores.js';

class SingleChart extends Component {
  disposeItem: null;
  disposeValues: null;

  componentWillMount() {
    // TODO - change dispose & re-listen on id change.
    this.disposeItem = Stores.singleStore.addListener(`${this.props.id}`, () => {
      console.log("Item changed!");
      this.forceUpdate();
    });
    this.disposeValues = Stores.valuesStore.addListener(`single/${this.props.id}`, () => {
      console.log("Values changed!");
      this.forceUpdate();
    });
  }

  componentWillUnmount() {
    this.disposeItem();
    this.disposeValues();
  }

  render() {
    const line = Stores.singleStore.get(this.props.id);
    const values = Stores.valuesStore.get(`single/${this.props.id}`);
    console.log("Loaded line = %O, values = %O", line, values);

    if (line === undefined || values === undefined) {
      return <LoadingIndicator />;
    }

    const lines = [{name: line.name, values}];
    return <LineChart lines={lines}/>;
  }
}

export default SingleChart;
