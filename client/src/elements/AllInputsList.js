import React, { Component } from 'react';

const moment = require('moment');

const TEST_DATA = [{
  name: 'hello',
  id: '1a2b3c4d',
  link: 'http://www.example.com',
}, {
  name: 'hello 2',
  id: '1a2b3c4d5e',
  link: 'http://www.example.com',
}];

class AllInputsList extends Component {
  render() {
    const lines = TEST_DATA;
    const noLinesMsg = lines.length > 0 ? null : (
      <span>Nothing to add - first create some <a href="/single">basic lines</a></span>
    );

    return (
      <div className="centralList allInput">
        <h2>Bulk entry</h2>
        {this.renderLinesIfAny()}
        {noLinesMsg}
      </div>
    );
  }

  renderLinesIfAny() {
    const lines = TEST_DATA;
    if (lines.length === 0) {
      return null;
    }
    const todayForInput = moment().format('YYYY-MM-DD');
    const submitHandler = this.addValues.bind(this);
    return (
      <form name="allInput" method="post" onSubmit={submitHandler}>
        <ul className="singleList">
          {lines.map(line => {
            const link = '/single/' + line.id; // HACK
            const inputName = 'in_' + line.id;
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
                  <input className="valueInput" type="number" step="any" name={inputName} tabIndex="1" />
                  {openLinkOrSpacer}
                </div>
                <div className="clear" />
              </div></li>
            );
          })}
        </ul>

        <hr className="fancy" />
        For date: <input type="date" name="time" value={todayForInput} />
        <input className="btn" type="submit" value="Go!" />
      </form>
    );
  }

  addValues(e) {
    e.preventDefault();
    console.log("Adding values...");
  }
}

export default AllInputsList;
