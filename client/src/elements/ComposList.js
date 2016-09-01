import React, { Component } from 'react';

import ComposForm from './ComposForm.js';

const TEST_DATA = [{
  name: 'hello',
  id: '1a2b3c4d',
}];

class ComposList extends Component {
  render() {
    const lines = TEST_DATA;
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

  delete(line) {
    console.log("Deleting...");
    console.log(line);
  }
}

export default ComposList;
