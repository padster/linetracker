import React, { Component } from 'react';

import GraphsForm from './GraphsForm.js';

import Store from '../data/Store.js';

class GraphsList extends Component {
  dispose: null;

  componentWillMount() {
    // TODO - change dispose & re-listen on id change.
    this.dispose = Store.addListener('graphs', () => {
      console.log("Changed!");
      this.forceUpdate();
    });
  }

  componentWillUnmount() {
    this.dispose();
  }

  render() {
    console.log("Rendering list of graphs");
    const lines = Store.listGraphs();
    console.log("Loaded in view: %O", lines);

    if (lines === undefined) {
      return this.renderLoading();
    }

    const noLineMsg = lines.length > 0 ? null : "No graphs! Create one below...";

    return (
      <div>
        <div className="centralList">
          <h2>Graphs</h2>

          <ul className="singlelist">
            {lines.map(line => {
              const url = 'graphs/' + line.id;
              const deleteHandler = this.delete.bind(this, line);
              const starHandler = this.star.bind(this, line);
              return (
                <li key={line.id}><div className="trow">
                  <div className="listingName"><a href={url}>{line.name}</a></div>
                  <div className="flex-spacer"></div>
                  <a className="listStar" title="Set Home Graph" onClick={starHandler}>
                    {line.isHome ? <i className="material-icons">star</i> : null}
                    {line.isHome ? null : <i className="material-icons starNormal">star_border</i>}
                    {line.isHome ? null : <i className="material-icons starHover">star_half</i>}
                  </a>
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
        <GraphsForm />
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
      Store.deleteGraphs(line.id);
    }
  }

  star(line) {
    console.log("Starring....");
    console.log(line);
  }
}

export default GraphsList;
