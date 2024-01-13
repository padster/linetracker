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
      this.forceUpdate();
    });
    this.disposeSingle = Stores.singleStore.addListener('', () => {
      this.forceUpdate();
    });
    this.disposeCompos = Stores.composStore.addListener('', () => {
      this.forceUpdate();
    });
    this.disposeValues = Stores.valuesStore.addListener('', () => {
      this.forceUpdate();
    });
    this.disposeSettings = Stores.settingsStore.addListener('', () => {
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

    const childrenWithNames = Stores.lookUpChildNames(graph.childMetadata);
    if (childrenWithNames === undefined) {
      return <LoadingIndicator />;
    }

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

    // Sort by value:
    const childToLastNonZero = {};
    childLines.forEach(line => {
      if (line.values == null) return;
      const lastNonZero = line.values.slice().reverse().find(p => p.v !== 0);
      if (lastNonZero == null) return;
      childToLastNonZero[line.name] = lastNonZero.v;
    });

    childLines.sort((a, b) => {
      const lastA = childToLastNonZero[a.name];
      const lastB = childToLastNonZero[b.name];
      if (lastA == null) return 1;
      if (lastB == null) return -1;
      return lastB - lastA;
    });

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
