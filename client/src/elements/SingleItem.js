import React, { Component } from 'react';

import Store from '../data/Store.js';

const moment = require('moment');

/*
const TEST_DATA = {
  values: [{
    t: '20160901',
    v: 123.6,
  },{
    t: '20160831',
    v: 121,
  }],
};
*/

class SingleItem extends Component {
  dispose: null;

  componentWillMount() {
    // TODO - change dispose & re-listen on id change.
    this.dispose = Store.addListener(`single/${this.props.id}`, () => {
      console.log("Changed!");
      this.forceUpdate();
    });
  }

  componentWillUnmount() {
    this.dispose();
  }

  render() {
    console.log("Rendering for " + this.props.id);
    const line = Store.getSingle(this.props.id);
    console.log("Loaded in view: %O", line);

    if (line === undefined) {
      return this.renderLoading();
    }

    const viewGraphLink = '/view/single/' + line.id;
    const noValuesMsg = line.values && line.values.length > 0 ? null :
        "No values, please enter them below...";
    const handleNewValues = this.insertValues.bind(this);
    const handleSetLink = this.setLink.bind(this);
    const todayForInput = moment().format('YYYY-MM-DD');

    return (
      <div className="centralList">
        <div className="listTitle">
          <h2>{line.name}</h2>
          <a className="btn btn-mini editName" href="#editNamePopup" title="Rename" data-toggle="modal">
            <i className="material-icons">mode_edit</i>
          </a>
          <div className="flex-spacer" />
          <a href={viewGraphLink} className="btn getView">
            Graph <i className="material-icons graphIcon">trending_up</i>
          </a>
        </div>

        <ul className="singleList">
          {line.values && line.values.map(value => {
            const formattedDay = moment(value.t, 'YYYYMMDD').format('YYYY, MMM DD');
            const handleDelete = () => this.deleteValue(line, value);
            return (
              <li key={value.t}><div className="trow">
                <div className="listingName"> {formattedDay}: {value.v} </div>
                <div className="flex-spacer" />
                <div className="listingActions">
                  <a className="btn btn-mini listRemove" title="Delete" onClick={handleDelete}>
                    <i className="material-icons">delete</i>
                  </a>
                </div>
              </div></li>
            );
          })}
          {noValuesMsg}
        </ul>

        <hr className="fancy" />

        <form name="values" method="post" onSubmit={handleNewValues}>
          Enter single value, with time: <br />
          <input type="number" step="any" name="value"></input>
          <input type="date" name="time" value={todayForInput}></input>
          <a className="btn btn-mini oneuplink" title="Permalink" href="#oneup/{{id}}">
            <i className="material-icons">link</i>
          </a>
          <br />

          <i>Or</i>, enter multiple at the same time: <br />
          <textarea name="bulk" className="span5" placeholder="Multiple values, one per line, each: dd/mm/yyyy,value" />
          <br />

          <input className="btn" type="submit" value="Add the value(s)"></input>
        </form>

        <form name="linkvalue" method="post" onSubmit={handleSetLink}>
        Optionally, Add a link for the resource: <br />
        <input type="text" name="link" value={line.link}></input>
        <input className="btn" type="submit" value="Set link"></input>
        </form>

        {/* TODO: UI for editing the name. */}
      </div>
    );
  }

  renderLoading() {
    // TODO
    return <span>"Loading..."</span>;
  }

  deleteValue(line, value) {
    console.log("Deleting %O from %O", value, line);
  }

  insertValues(e) {
    e.preventDefault();
    console.log("TODO: insert new values");
  }

  setLink(e) {
    e.preventDefault();
    console.log("TODO: set link");
  }
}

export default SingleItem;
