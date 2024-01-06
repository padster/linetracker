import React, { Component } from 'react';

import LoadingIndicator from './LoadingIndicator.js';
import SingleForm from './SingleForm.js';

import Stores from '../data/Stores.js';

class SingleList extends Component {
  dispose: null;

  componentDidMount() {
    // TODO - change dispose & re-listen on id change.
    this.dispose = Stores.singleStore.addListener('', () => {
      console.log("Changed!");
      this.forceUpdate();
    });
  }

  componentWillUnmount() {
    this.dispose();
  }

  render() {
    console.log("Rendering list of single");
    const loadedLines = Stores.singleStore.list();

    if (loadedLines === undefined) {
      return <LoadingIndicator />;
    }

    const lines = loadedLines.sort((a, b) => {
      if (a.link && !b.link) { return -1; }
      if (b.link && !a.link) { return 1; }
      return a.name.localeCompare(b.name);
    });
    console.log("Loaded in view: %O", lines);

    const noLineMsg = lines.length > 0 ? null : "No basic lines! Create one below...";

    return (
      <div>
        <div className="centralList">
          <h2>Basic Lines</h2>

          <ul className="singlelist">
            {lines.map(line => {
              const url = 'single/' + line.id;
              const deleteHandler = this.delete.bind(this, line);
              const linkRender = line.link && (
                <a className="btn btn-mini viewlink" title="View" href={line.link} target="_blank" rel="noreferrer">
                  <i className="material-icons">open_in_new</i>
                </a>
              );
              return (
                <li key={line.id}><div className="trow">
                  <div className="listingName"><a href={url}>{line.name}</a></div>
                  <div className="flex-spacer"></div>
                  <div className="listingActions">
                    {linkRender}
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
        <SingleForm onCreate={this.create.bind(this)}/>
      </div>
    );
  }

  create(lineMeta: Object) {
    Stores.singleStore.create(lineMeta, newLine => {
      // TODO - navigator
      window.location.replace(`/single/${newLine.id}`);
    });
  }

  delete(line) {
    console.log(`Deleting /single/${line.id}`);
    if (window.confirm("Deleting is permanent, are you sure?")) {
      Stores.singleStore.delete(line.id);
    }
  }
}

export default SingleList;
