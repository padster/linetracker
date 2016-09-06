import React, { Component } from 'react';

import SingleForm from './SingleForm.js';

import Store from '../data/Store.js';

class SingleList extends Component {
  dispose: null;

  componentWillMount() {
    // TODO - change dispose & re-listen on id change.
    this.dispose = Store.addListener('single', () => {
      console.log("Changed!");
      this.forceUpdate();
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
                <a className="btn btn-mini viewlink" title="View" href={line.link} target="_blank">
                  <i className="material-icons">open_in_new</i>
                </a>
              );
              return (
                <li key={line.id}><div className="trow">
                  <div className="listingName"><a href={url}>{line.name}</a></div>
                  <div className="flex-spacer"></div>
                  <div className="listingActions">
                    {linkRender}
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
        <SingleForm />
      </div>
    );
  }

  renderLoading() {
    // TODO
    return <span>"Loading..."</span>;
  }

  delete(line) {
    console.log(`Deleting /single/${line.id}`);
    if (window.confirm("Deleting is permanent, are you sure?")) {
      Store.deleteSingle(line.id, () => {
        // HACK - use central navigator.
        this.forceUpdate();
      });
    }
  }
}

export default SingleList;
