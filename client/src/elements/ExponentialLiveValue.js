import React, { Component } from 'react';

import LiveTicker from '../data/LiveTicker.js';

class ExponentialLiveValue extends Component {
  dispose: null;

  componentDidMount() {
    this.dispose = LiveTicker.addListener(this.forceUpdate.bind(this));
  }

  componentWillUnmount() {
    this.dispose();
  }

  render() {
    const t = new Date().getTime();
    let val = parseFloat(this.props.a) + t * parseFloat(this.props.b);
    val = Math.exp(val) + (parseFloat(this.props.c) || 0);
    const parts = val.toFixed(2).toString().split(".");
    const formatted = '$' + parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "." + parts[1];
    return <span className="liveTicker">{formatted}</span>
  }
}

export default ExponentialLiveValue;
