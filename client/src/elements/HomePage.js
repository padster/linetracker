import React, { Component } from 'react';

import ExponentialLiveValue from './ExponentialLiveValue.js';
import LineChart from './LineChart.js';

const TEST_DATA = [{
  name: 'test',
  id: 'a1b2c3',
  fit: [-20.631857606874597, 2.2045431116139573e-11, -12619.249955085863],
}];

class HomePage extends Component {
  render() {
    const estimates = TEST_DATA;

    const noLinesMsg = estimates.length > 0 ? null : (
      <pre>
      You have no home graph selected!

      Visit <a href="/#graphs">Graphs</a> to select one, or read the <a href="/pages/help.html">FAQ</a> if you are new.
      </pre>
    );

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
  }

  maybeDrawGraph(estimates) {
    if (estimates.length === 0) {
      return null;
    }
    return <LineChart />;
  }
}

export default HomePage;
