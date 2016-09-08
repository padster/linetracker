import React, { Component } from 'react';
import { Button } from 'react-bootstrap';

import NameModal from './NameModal.js';

import Stores from '../data/Stores.js';

const moment = require('moment');

class SingleItem extends Component {
  state: Object;
  dispose: null;

  componentWillMount() {
    this.state = {
      singleValueAmount: '',
      singleValueDate: moment().format('YYYY-MM-DD'),
      multipleValueText: '',
      newLinkURL: null,

      editNameOpen: false,
    };

    // TODO - change dispose & re-listen on id change.
    this.dispose = Stores.singleStore.addListener(`${this.props.id}`, () => {
      console.log("Changed!");
      this.forceUpdate();
    });
  }

  componentWillUnmount() {
    this.dispose();
  }

  render() {
    console.log("Rendering for " + this.props.id);
    const line = Stores.singleStore.get(this.props.id);
    console.log("Loaded in view: %O", line);

    if (line === undefined) {
      return this.renderLoading();
    }

    const viewGraphLink = '/view/single/' + line.id;
    const noValuesMsg = line.values && line.values.length > 0 ? null :
        "No values, please enter them below...";
    const handleNewValues = this.insertValues.bind(this);
    const handleSetLink = this.setLink.bind(this);
    const linkToShow = this.state.newLinkURL !== null ? this.state.newLinkURL : line.link;
    const openEditName = () => this.setState({editNameOpen: true});

    return (
      <div className="centralList">
        <div className="listTitle">
          <h2>{line.name}</h2>
          <Button bsSize="xsmall" className="btn-mini" title="Rename" onClick={openEditName}>
            <i className="material-icons">mode_edit</i>
          </Button>
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
          <input type="number"
            step="any"
            value={this.state.singleValueAmount}
            onChange={e => this.setState({singleValueAmount: e.target.value})}
          />
          <input type="date"
            value={this.state.singleValueDate}
            onChange={e => this.setState({singleValueDate: e.target.value})}
          />
          {/* TODO: support one-up page. */}
          <a className="btn btn-mini oneuplink" title="Permalink" href="oneup/{{id}}">
            <i className="material-icons">link</i>
          </a>
          <br />

          <i>Or</i>, enter multiple at the same time: <br />
          <textarea
            className="span5"
            placeholder="Multiple values, one per line, each: dd/mm/yyyy,value"
            value={this.state.multipleValueText}
            onChange={e => this.setState({multipleValueText: e.target.value})}
          />
          <br />

          <input className="btn" type="submit" value="Add the value(s)"></input>
        </form>

        <form name="linkvalue" method="post" onSubmit={handleSetLink}>
        Optionally, Add a link for the resource: <br />
        <input type="text"
          value={linkToShow}
          onChange={e => this.setState({newLinkURL: e.target.value})}
        />
        <input className="btn" type="submit" value="Set link" />
        </form>

        <NameModal
          defaultValue={line.name}
          show={this.state.editNameOpen}
          onHide={() => this.setState({editNameOpen: false})}
          onChange={this.changeName.bind(this)}
        />
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
    console.log(`TODO: set link to ${this.state.newLinkURL}`);
  }

  changeName(name) {
    console.log(`TODO: change name to ${name}`);
  }
}

export default SingleItem;
