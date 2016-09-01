import React, { Component } from 'react';

const TEST_DATA = [{
  name: 'hello',
  id: '1a2b3c4d',
  link: 'http://www.example.com',
}];

class SingleList extends Component {
  render() {
    const lines = TEST_DATA;
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
        {/*TODO: SingleForm */}
      </div>
    );
  }

  delete(line) {
    console.log("Deleting...");
    console.log(line);
  }
}

export default SingleList;
