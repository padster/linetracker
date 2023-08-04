import React, { Component } from 'react';

import GraphsForm from './GraphsForm.js';
import LoadingIndicator from './LoadingIndicator.js';

import Stores from '../data/Stores.js';

class GraphsList extends Component {
  dispose: null;
  disposeSettings: null;

  componentDidMount() {
    // TODO - change dispose & re-listen on id change.
    this.dispose = Stores.graphsStore.addListener('', () => {
      console.log("Changed!");
      this.forceUpdate();
    });
    this.disposeSettings = Stores.settingsStore.addListener('', () => {
      console.log("Settings changed!");
      this.forceUpdate();
    });
  }

  componentWillUnmount() {
    this.dispose();
    this.disposeSettings();
  }

  render() {
    console.log("Rendering list of graphs");
    const loadedLines = Stores.graphsStore.list();

    if (loadedLines === undefined) {
      return <LoadingIndicator />;
    }

    const lines = loadedLines.sort((a, b) => {
      return a.name.localeCompare(b.name);
    });
    console.log("Loaded in view: %O", lines);

    const settings = Stores.settingsStore.get();
    if (settings === undefined) {
      return <LoadingIndicator />;
    }

    const noLineMsg = lines.length > 0 ? null : "No graphs! Create one below...";

    return (
      <div>
        <div className="centralList">
          <h2>Graphs</h2>

          <ul className="singlelist">
            {lines.map(line => {
              const url = 'graphs/' + line.id;
              const isHome = line.id === settings.homeID;
              const deleteHandler = this.delete.bind(this, line);
              const starHandler = this.star.bind(this, line, settings);
              return (
                <li key={line.id}><div className="trow">
                  <div className="listingName"><a href={url}>{line.name}</a></div>
                  <div className="flex-spacer"></div>
                  <button className="listStar btn btn-mini" title="Set Home Graph" onClick={starHandler}>
                    {isHome ? <i className="material-icons">star</i> : null}
                    {isHome ? null : <i className="material-icons starNormal">star_border</i>}
                    {isHome ? null : <i className="material-icons starHover">star_half</i>}
                  </button>
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
        <GraphsForm onCreate={this.create.bind(this)} />
      </div>
    );
  }

  create(lineMeta: Object) {
    Stores.graphsStore.create(lineMeta, newLine => {
      // TODO - navigator
      window.location.replace(`/graphs/${newLine.id}`);
    });
  }

  delete(line) {
    console.log(`Deleting /compos/${line.id}`);
    if (window.confirm("Deleting is permanent, are you sure?")) {
      Stores.graphsStore.delete(line.id);
    }
  }

  star(line, settings) {
    console.log("Starring....");
    settings.homeID = line.id;
    Stores.settingsStore.update(settings);
  }
}

export default GraphsList;
