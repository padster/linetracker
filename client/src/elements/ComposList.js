import React, { Component } from 'react';

import ComposForm from './ComposForm.js';
import LoadingIndicator from './LoadingIndicator.js';

import Stores from '../data/Stores.js';

class ComposList extends Component {
  dispose: null;

  componentDidMount() {
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
    const loadedLines = Stores.composStore.list();

    if (loadedLines === undefined) {
      return <LoadingIndicator />;
    }

    const lines = loadedLines.sort((a, b) => {
      return a.name.localeCompare(b.name);
    });
    console.log("Loaded in view: %O", lines);

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
                    <button className="btn btn-mini listRemove" title="Delete" onClick={deleteHandler}>
                      <i className="material-icons">delete</i>
                    </button>
                  </div>
                </div></li>
              );
            })}
            {noLineMsg}
          </ul>
        </div>

        <hr className="fancy" />
        <ComposForm onCreate={this.create.bind(this)} />
      </div>
    );
  }

  create(lineMeta: Object) {
    Stores.composStore.create(lineMeta, newLine => {
      // TODO - navigator
      window.location.replace(`/compos/${newLine.id}`);
    });
  }

  delete(line) {
    console.log(`Deleting /compos/${line.id}`);
    if (window.confirm("Deleting is permanent, are you sure?")) {
      Stores.composStore.delete(line.id);
    }
  }
}

export default ComposList;
