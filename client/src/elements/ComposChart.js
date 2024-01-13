import React, { Component } from 'react';

import LineChart from './LineChart.js';
import LoadingIndicator from './LoadingIndicator.js';

import Stores from '../data/Stores.js';

class SingleChart extends Component {
  disposeItem: null;
  disposeValues: null;

  componentDidMount() {
    // TODO - change dispose & re-listen on id change.
    this.disposeItem = Stores.composStore.addListener(`${this.props.id}`, () => {
      this.forceUpdate();
    });
    this.disposeValues = Stores.valuesStore.addListener(`compos/${this.props.id}`, () => {
      this.forceUpdate();
    });
  }

  componentWillUnmount() {
    this.disposeItem();
    this.disposeValues();
  }

  render() {
    const line = Stores.composStore.get(this.props.id);
    const values = Stores.valuesStore.get(`compos/${this.props.id}`);

    if (line === undefined || values === undefined) {
      return <LoadingIndicator />;
    }

    const lines = [{name: line.name, values}];
    return <LineChart lines={lines}/>;
  }
}

export default SingleChart;
