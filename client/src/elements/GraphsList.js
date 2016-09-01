import React, { Component } from 'react';

const TEST_DATA = [{
  name: 'hello',
  id: '1a2b3c4d',
  link: 'http://www.example.com',
}];

class GraphsList extends Component {
  render() {
    const lines = TEST_DATA;
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
        {/*TODO: GraphsForm */}
      </div>
    );
  }

  delete(line) {
    console.log("Deleting...");
    console.log(line);
  }

  star(line) {
    console.log("Starring....");
    console.log(line);
  }
}

export default GraphsList;
