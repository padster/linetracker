import React, { Component } from 'react';
import { Button } from 'react-bootstrap';

import ChildPickerModal from './ChildPickerModal.js';
import LoadingIndicator from './LoadingIndicator.js';
import NameModal from './NameModal.js';

import Stores from '../data/Stores.js';

class ComposItem extends Component {
  state: Object;
  dispose: null;

  // Needed to look up child names.
  disposeSingleList: null;
  disposeComposList: null;

  componentWillMount() {
    this.state = {
      addLineOpen: false,
      editNameOpen: false,
    };

    // TODO - change dispose & re-listen on id change.
    this.dispose = Stores.composStore.addListener(`${this.props.id}`, () => {
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
    const line = Stores.composStore.get(this.props.id);
    console.log("Loaded in view: %O", line);

    if (line === undefined) {
      return <LoadingIndicator />;
    }

    const childrenWithNames = Stores.lookUpChildNames(line.childMetadata);
    if (childrenWithNames === undefined) {
      return <LoadingIndicator />;
    }

    const viewGraphLink = '/view/compos/' + line.id;
    const noChildrenMsg = line.childMetadata.length > 0 ? null :
        "No lines, please add some below...";
    const openAddLine = () => this.setState({addLineOpen: true});
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
            {childrenWithNames.map((child, i) => {
              const handleDelete = this.deleteChild.bind(this, line, child);
              const childLink = `/${child.type}/${child.id}`;
              return (
                <li key={i}><div className="trow">
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
        <Button onClick={openAddLine}>Add line</Button>

        <NameModal
          defaultValue={line.name}
          show={this.state.editNameOpen}
          onHide={() => this.setState({editNameOpen: false})}
          onChange={this.changeName.bind(this)}
        />

        <ChildPickerModal
          show={this.state.addLineOpen}
          onHide={() => this.setState({addLineOpen: false})}
          onPick={this.addChildren.bind(this, line.fullID)}
          filterLine={line.fullID}
        />
      </div>
    );
  }

  changeName(name) {
    console.log(`TODO: change name to ${name}`);
  }

  addChildren(fullID: String, children: Array<String>) {
    const asEntries = children.map(child => {
      const childParts = child.split('/');
      return {
        type: childParts[0],
        id: childParts[1],
      };
    })
    Stores.childStore.addChildren(fullID, asEntries);
  }

  deleteChild(line, child) {
    console.log("Deleting %O from %O", child, line);
    const asEntry = {
      type: child.type,
      id: child.id,
    };
    Stores.childStore.removeChild(line.fullID, asEntry);
  }
}

export default ComposItem;
