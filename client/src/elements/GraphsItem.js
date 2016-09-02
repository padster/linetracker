import React, { Component } from 'react';

const TEST_DATA = {
  name: 'hello',
  childMetadata: [{
    name: 'c1',
    id: 'childid1',
  }, {
    name: 'c2',
    id: 'childid2',
  }],
};

class GraphsItem extends Component {
  render() {
    const line = TEST_DATA;
    line.id = this.props.id;
    const viewGraphLink = '/view/graphs/' + line.id;
    const noChildrenMsg = line.childMetadata.length > 0 ? null :
        "No lines, please add some below...";

    return (
      <div>
        <div className="centralList">
          <div className="listTitle">
            <h2>{line.name}</h2>
            <a className="btn btn-mini editName" href="#editNamePopup" title="Rename" data-toggle="modal">
              <i className="material-icons">mode_edit</i>
            </a>
            <div className="flex-spacer" />
            <a href={viewGraphLink} className="btn getView">
              Graph <i className="material-icons graphIcon">trending_up</i>
            </a>
          </div>

          <ul className="singleList">
            {line.childMetadata.map(child => {
              const handleDelete = this.deleteChild.bind(this, line, child);
              const childLink = '/single/' + child.id; // HACK
              return (
                <li key={child.id}><div className="trow">
                  <div className="listingName">
                    <a href={childLink}>{child.name}</a>
                  </div>
                  <div className="flex-spacer" />
                  <div className="listingActions">
                    <a className="btn btn-mini listRemove" title="Delete" onClick={handleDelete}>
                      <i className="material-icons">delete</i>
                    </a>
                  </div>
                </div></li>
              );
            })}
            {noChildrenMsg}
          </ul>
        </div>

        <hr className="fancy" />
        <div>
          <a href="#valueList" className="btn" data-toggle="modal">Add line</a>
        </div>

        {/* TODO: UI for editing the name. UI for adding a line */}
      </div>
    );
  }

  deleteChild(line, child) {
    console.log("Deleting %O from %O", child, line);
  }
}

export default GraphsItem;
