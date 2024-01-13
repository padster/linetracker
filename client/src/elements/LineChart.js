import React, { Component } from 'react';

import ExponentialFit from '../data/ExponentialFit.js';

const moment = require('moment');

// jQuery flot options: https://github.com/flot/flot/blob/master/API.md#plot-options
const FIXED_OPTIONS = {
  series: {
    lines: { show: true },
    points: { show: true },
  },
  grid: { hoverable: true, clickable: true },
  xaxis: { mode: "time" },
  legend: { show: true, position: 'nw' }
};

class LineChart extends Component {
  componentDidMount() {
    this.drawChart();
  }
  componentDidUpdate() {
    this.drawChart();
  }

  render() {
    return (
      <div className='graphHolder'>
        <div className='graph' ref={r => {this.graph = r;}} />
        <div id='tooltip' style={{display: 'none', position: 'absolute', padding: '4px'}}></div>
      </div>
    );
  }

  drawChart() {
    const asLines = this.convertDataToFlot(this.props.lines);
    const options = this.buildOptions(asLines);
    window.$.plot(this.graph, asLines, options);
    window.$(this.graph).bind("plotclick", function(event, pos, item) {
      if (!item) {
        window.$("#tooltip").hide();
        return;
      }

      var x = item.datapoint[0]; // Date moment
      var y = item.datapoint[1].toFixed(2); // Value
      const message = `${moment(x).format('DD/MM/YYYY')}<br />${item.series.label} = ${y}`;
      window.$("#tooltip")
        .html(message)
        .css({
          top: item.pageY+5, 
          left: item.pageX+5,
          border: `1px solid ${item.series.color}`,
          backgroundColor: 'white',
        })
        .fadeIn(200);
    });
  }

  convertDataToFlot(lines) {
    const trimmedLines = this.trimEdgeZeros(lines);

    // Find values for each non-const line
    let asLines = trimmedLines.filter(x => x.values != null).map(function(x) { return {
      'label': x.name,
      'data': x.values.map(function(y) { return [moment(y.t, 'YYYYMMDD'), y.v]; })
    }});

    // Add const lines:
    // 1) Set lastTime to the current YYYYMMDD, and firstTime to 1 day before...
    var firstTime = null, lastTime = null;
    trimmedLines.forEach((line, i) => {
      if (line.values == null || line.values.length === 0) {
        return;
      }
      const first = line.values[0].t;
      const last = line.values[line.values.length - 1].t;
      firstTime = (firstTime === null ? first : Math.min(firstTime, first));
      lastTime = (lastTime === null ? last : Math.max(lastTime, last));
    });

    if (firstTime == null && lastTime == null) {
      lastTime = moment().format('YYYYMMDD');
      firstTime = moment(lastTime, 'YYYYMMDD').subtract(1, 'days');
    }

    // 2) ...and add const line between those dates:
    const constLines = trimmedLines.filter(x => x.values == null).map(function(x) { return {
      'label': x.name,
      'data': [[moment(firstTime, 'YYYYMMDD'), x.constValue], [moment(lastTime, 'YYYYMMDD'), x.constValue]]
    }});
    asLines = asLines.concat(constLines);

    /* PICK: Exponential fit
    if (asLines.length == 1 && W.urlParam('fit') == 1) {
      asLines.push({
        'label': 'Exp. fit',
        'data': M.bestFitPoints(asLines[0].data)
      });
    };
    */

    return asLines;
  }

  buildOptions(asLines) {
    const opt = FIXED_OPTIONS;

    // Find X-axis tick size:
    var firstTime = null, lastTime = null;
    asLines.forEach((line, i) => {
      if (line.data === null || line.data.length === 0) {
        return;
      }
      const first = line.data[0][0];
      const last = line.data[line.data.length - 1][0];
      firstTime = (firstTime === null ? first : Math.min(firstTime, first));
      lastTime = (lastTime === null ? last : Math.max(lastTime, last));
    });
    if (firstTime == null && lastTime == null) {
      // No data! Show today +/- 15 days
      const dayPadding = 15 * 24 * 60 * 60 * 1000;
      firstTime = new Date().getTime() - dayPadding;
      lastTime = firstTime + 2 * dayPadding;
    }
    // Always a month, unless it's under 1.5 months in view.
    const minTick = (lastTime - firstTime < 45 * 24 * 60 * 60 * 1000) ? [1, "day"] : [1, "month"];
    opt.xaxis.minTickSize = minTick;
    return opt;

    // PICK: log scale?
    /*
    if (W.urlParam('log') == 1) {
      options.yaxis = {
        transform: function(v) {
          return v < 1 ? 0 : Math.log(v);
        }
      }
    }
    */
  }

  trimEdgeZeros(lines) {
    return lines.map(line => {
      if (line.values == null) {
        return line;
      }
      return Object.assign({}, line, {
        values: ExponentialFit.trimEdgeZeros(line.values, 'v')
      });
    });
  }
}

export default LineChart;
