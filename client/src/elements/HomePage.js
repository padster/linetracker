import React, { Component } from 'react';

import ExponentialLiveValue from './ExponentialLiveValue.js';
import LineChart from './LineChart.js';
import LoadingIndicator from './LoadingIndicator.js';

import ExponentialFit from '../data/ExponentialFit.js';
import Stores from '../data/Stores.js';

const moment = require('moment');

/*
const TEST_DATA = [{
  name: 'test',
  id: 'a1b2c3',
  fit: [-20.631857606874597, 2.2045431116139573e-11, -12619.249955085863],
}];
*/

class HomePage extends Component {
  disposeGraphs: null;
  disposeSingle: null;
  disposeCompos: null;
  disposeValues: null;
  disposeSettings: null;

  componentDidMount() {
    this.disposeGraphs = Stores.graphsStore.addListener('', () => {
      console.log("Graphs updated!");
      this.forceUpdate();
    });
    this.disposeSingle = Stores.singleStore.addListener('', () => {
      console.log("Single updated!");
      this.forceUpdate();
    });
    this.disposeCompos = Stores.composStore.addListener('', () => {
      console.log("Compos updated!");
      this.forceUpdate();
    });
    this.disposeValues = Stores.valuesStore.addListener('', () => {
      console.log("Values changed!");
      this.forceUpdate();
    });
    this.disposeSettings = Stores.settingsStore.addListener('', () => {
      console.log("Settings changed!");
      this.forceUpdate();
    });
  }

  componentWillUnmount() {
    this.disposeGraphs();
    this.disposeSingle();
    this.disposeCompos();
    this.disposeValues();
    this.disposeSettings();
  }

  render() {
    const settings = Stores.settingsStore.get();
    if (settings === undefined) {
      return <LoadingIndicator />;
    }
    if (!settings.homeID) {
      return this.renderNoHomeGraphMsg();
    }

    const graph = Stores.graphsStore.get(settings.homeID);
    if (graph === undefined) {
      return <LoadingIndicator />;
    }

    console.log(graph);

    const childrenWithNames = Stores.lookUpChildNames(graph.childMetadata);
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

    return (
      <div>
        <div className="totalEstimate navbar-inner">
          {childLines.map((line, i) => {
            const valuesAsXY = line.values.map(p => {
              return [moment(p.t, 'YYYYMMDD').valueOf(), p.v];
            })
            const expFit = ExponentialFit.bestFitCoeffs(valuesAsXY);
            return (
              <div key={i} className="estimateRow">
                {line.name}
                <ExponentialLiveValue a={expFit[0]} b={expFit[1]} c={expFit[2]} />
                <div className="clear" />
              </div>
            );
          })}
        </div>

        {this.maybeDrawGraph(childLines)}
      </div>
    );
  }

  renderNoHomeGraphMsg() {
   return (
      <pre>
      You have no home graph selected!

      Visit <a href="/#graphs">Graphs</a> to select one, or read the <a href="/pages/help.html">FAQ</a> if you are new.
      </pre>
    );
  }

  maybeDrawGraph(childLines) {
    if (childLines.length === 0) {
      return null;
    }
    return <LineChart lines={childLines}/>;
  }
}

export default HomePage;
