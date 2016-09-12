import React, { Component } from 'react';

import LoadingIndicator from './LoadingIndicator.js';
import NameModal from './NameModal.js';

import Stores from '../data/Stores.js';

class GraphsItem extends Component {
  state: Object;
  dispose: null;

  // Needed to look up child names.
  disposeSingleList: null;
  disposeComposList: null;

  componentWillMount() {
    this.state = {
      editNameOpen: false,
    };

    // TODO - change dispose & re-listen on id change.
    this.dispose = Stores.graphsStore.addListener(`${this.props.id}`, () => {
      console.log("Changed!");
      this.forceUpdate();
    });
    this.disposeSingleList = Stores.singleStore.addListener('', () => {
      console.log("Single list updated!");
      this.forceUpdate();
    });
    this.disposeComposList = Stores.composStore.addListener('', () => {
      console.log("Compos list updated!");
      this.forceUpdate();
    });
  }

  componentWillUnmount() {
    this.disposeComposList();
    this.disposeSingleList();
    this.dispose();
  }

  render() {
    console.log("Rendering for " + this.props.id);
    const line = Stores.graphsStore.get(this.props.id);
    console.log("Loaded in view: %O", line);

    if (line === undefined) {
      return <LoadingIndicator />;
    }

    const childrenWithNames = Stores.lookUpChildNames(line.childMetadata);
    if (childrenWithNames === undefined) {
      return <LoadingIndicator />;
    }

    const viewGraphLink = '/view/graphs/' + line.id;
    const noChildrenMsg = line.childMetadata.length > 0 ? null :
        "No lines, please add some below...";
    const openEditName = () => this.setState({editNameOpen: true});

    return (
      <div>
        <div className="centralList">
          <div className="listTitle">
            <h2>{line.name}</h2>
            <span className="btn btn-mini editName" title="Rename" onClick={openEditName}>
              <i className="material-icons">mode_edit</i>
            </span>
            <div className="flex-spacer" />
            <a href={viewGraphLink} className="btn getView">
              Graph <i className="material-icons graphIcon">trending_up</i>
            </a>
          </div>

          <ul className="singleList">
            {childrenWithNames.map(child => {
              const handleDelete = this.deleteChild.bind(this, line, child);
              const childType = child.type === 's' ? 'single' : 'compos'; // Only two so far.
              const childLink = `/${childType}/${child.id}`;
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

        <NameModal
          defaultValue={line.name}
          show={this.state.editNameOpen}
          onHide={() => this.setState({editNameOpen: false})}
          onChange={this.changeName.bind(this)}
        />

        {/* TODO: UI for adding a line */}
      </div>
    );
  }

  changeName(name) {
    console.log(`TODO: change name to ${name}`);
  }

  deleteChild(line, child) {
    console.log("Deleting %O from %O", child, line);
  }
}

export default GraphsItem;
