import React, { Component } from 'react';

import ComposForm from './ComposForm.js';

import Stores from '../data/Stores.js';

class ComposList extends Component {
  dispose: null;

  componentWillMount() {
    // TODO - change dispose & re-listen on id change.
    this.dispose = Stores.composStore.addListener('', () => {
      console.log("Changed!");
      this.forceUpdate();
    });
  }

  componentWillUnmount() {
    this.dispose();
  }

  render() {
    console.log("Rendering list of compos");
    const lines = Stores.composStore.list();
    console.log("Loaded in view: %O", lines);

    if (lines === undefined) {
      return this.renderLoading();
    }

    const noLineMsg = lines.length > 0 ? null : "No calculated lines! Create one below...";

    return (
      <div>
        <div className="centralList">
          <h2>Calculated Lines</h2>

          <ul className="singlelist">
            {lines.map(line => {
              const url = 'compos/' + line.id;
              const deleteHandler = this.delete.bind(this, line);
              return (
                <li key={line.id}><div className="trow">
                  <div className="listingName"><a href={url}>{line.name}</a></div>
                  <div className="flex-spacer"></div>
                  <div className="listingActions">
                    <a className="btn btn-mini listRemove" title="Delete" onClick={deleteHandler}>
                      <i className="material-icons">delete</i>
                    </a>
                  </div>
                </div></li>
              );
            })}
            {noLineMsg}
          </ul>
        </div>

        <hr className="fancy" />
        <ComposForm />
      </div>
    );
  }

  renderLoading() {
    // TODO
    return <span>"Loading..."</span>;
  }

  delete(line) {
    console.log(`Deleting /compos/${line.id}`);
    if (window.confirm("Deleting is permanent, are you sure?")) {
      Stores.composStore.delete(line.id);
    }
  }
}

export default ComposList;
