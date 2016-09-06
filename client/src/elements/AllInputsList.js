import React, { Component } from 'react';

import Store from '../data/Store.js';

const moment = require('moment');

class AllInputsList extends Component {
  state: null;
  dispose: null;

  componentWillMount() {
    this.state = {
      values: [],
      entryDate: moment().format('YYYY-MM-DD'),
    };

    // TODO - change dispose & re-listen on id change.
    this.dispose = Store.addListener('single', () => {
      console.log("Changed!");
      const allLines = Store.listSingle();
      this.setState({values: allLines.map(line => '')});
    });
  }

  componentWillUnmount() {
    this.dispose();
  }

  render() {
    console.log("Rendering list of single");
    const lines = Store.listSingle();
    console.log("Loaded in view: %O", lines);

    if (lines === undefined) {
      return this.renderLoading();
    }

    const noLinesMsg = lines.length > 0 ? null : (
      <span>Nothing to add - first create some <a href="/single">basic lines</a></span>
    );

    return (
      <div className="centralList allInput">
        <h2>Bulk entry</h2>
        {this.renderLinesIfAny(lines)}
        {noLinesMsg}
      </div>
    );
  }

  renderLinesIfAny(lines: Array<Object>) {
    if (lines.length === 0) {
      return null;
    }

    const submitHandler = this.addValues.bind(this);
    return (
      <form name="allInput" method="post" onSubmit={submitHandler}>
        <ul className="singleList">
          {lines.map((line, idx) => {
            const link = '/single/' + line.id; // HACK
            const value = this.state.values[idx];

            const openLinkOrSpacer = !line.link ? <span className="buttonSpacer" /> : (
              <a className="btn btn-mini viewlink" href={line.link} target="_blank" tabIndex="2">
                <i className="material-icons">open_in_new</i>
              </a>
            );

            return (
              <li key={line.id}><div className="trow">
                <div className="listingName">
                  <a href={link} target="_blank">{line.name}</a>
                </div>
                <div className="flex-spacer" />
                <div className="listingActions">
                  <input type="number"
                    className="valueInput"
                    step="any"
                    tabIndex="1"
                    value={value}
                    onChange={e => {
                      const valuesCopy = [...this.state.values];
                      valuesCopy[idx] = e.target.value;
                      this.setState({values: valuesCopy});
                    }} />
                  {openLinkOrSpacer}
                </div>
                <div className="clear" />
              </div></li>
            );
          })}
        </ul>

        <hr className="fancy" />
        For date: &nbsp;
        <input type="date"
          value={this.state.entryDate}
          onChange={e => this.setState({entryDate: e.target.value})}
        />
        <input className="btn" type="submit" value="Go!" />
      </form>
    );
  }

  renderLoading() {
    // TODO
    return <span>"Loading..."</span>;
  }

  addValues(e) {
    e.preventDefault();
    console.log(`Adding ${JSON.stringify(this.state.values)} for ${this.state.entryDate}`);
  }
}

export default AllInputsList;
