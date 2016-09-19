import React, { Component } from 'react';

// import ExponentialLiveValue from './ExponentialLiveValue.js';
import LineChart from './LineChart.js';
import LoadingIndicator from './LoadingIndicator.js';

import Stores from '../data/Stores.js';

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

  componentWillMount() {
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

    return <LineChart lines={childLines}/>;

    /*
    const estimates = TEST_DATA;

    return (
      <div>
        <div className="totalEstimate navbar-inner">
          {estimates.map(line => {
            return (
              <div key={line.id} className="estimateRow">
                {line.name}
                <ExponentialLiveValue a={line.fit[0]} b={line.fit[1]} c={line.fit[2]} />
                <div className="clear" />
              </div>
            );
          })}
          {noLinesMsg}
        </div>

        {this.maybeDrawGraph(estimates)}
      </div>
    );
    */
  }

  renderNoHomeGraphMsg() {
   return (
      <pre>
      You have no home graph selected!

      Visit <a href="/#graphs">Graphs</a> to select one, or read the <a href="/pages/help.html">FAQ</a> if you are new.
      </pre>
    );
  }

  maybeDrawGraph(estimates) {
    if (estimates.length === 0) {
      return null;
    }
    return <LineChart />;
  }
}

export default HomePage;
