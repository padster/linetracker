import React, { Component } from 'react';

import LineChart from './LineChart.js';
import LoadingIndicator from './LoadingIndicator.js';

import Stores from '../data/Stores.js';

class GraphsChart extends Component {
  disposeItem: null;
  disposeValues: null;
  disposeSingleList: null;
  disposeComposList: null;

  componentDidMount() {
    // TODO - change dispose & re-listen on id change.
    this.disposeItem = Stores.graphsStore.addListener(`${this.props.id}`, () => {
      this.forceUpdate();
    });
    this.disposeValues = Stores.valuesStore.addListener(``, () => {
      this.forceUpdate();
    });
    this.disposeSingleList = Stores.singleStore.addListener('', () => {
      this.forceUpdate();
    });
    this.disposeComposList = Stores.composStore.addListener('', () => {
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
    const line = Stores.graphsStore.get(this.props.id);
    if (line === undefined) {
      return <LoadingIndicator />;
    }

    const childrenWithNames = Stores.lookUpChildNames(line.childMetadata);
    if (childrenWithNames === undefined) {
      return <LoadingIndicator />;
    }

    let allChildValues = true;
    const childLines = childrenWithNames.map(child => {
      if (child.type === 'const') {
        return {name: child.name, constValue: child.value};
      } else {
        const childValues = Stores.valuesStore.get(`${child.type}/${child.id}`);
        if (childValues === undefined || childValues === null) {
          allChildValues = false;
        }
        return {name: child.name, values: childValues};
      }
    });

    if (!allChildValues) {
      return <LoadingIndicator />;
    }

    return <LineChart lines={childLines}/>;
  }
}

export default GraphsChart;
