import React, { Component } from 'react';
import { Button, Modal } from 'react-bootstrap';

import Stores from '../data/Stores.js';

/** UI to show a modal popup for editing a line's name. */
class ChildPickerModal extends Component {
  state: Object;

  disposeSingleList: null;
  disposeComposList: null;

  componentWillMount() {
    this.state = {
      selectedChildren: new Set(),
    };

    this.disposeSingleList = Stores.singleStore.addListener('', () => {
      this.forceUpdate();
    });
    this.disposeComposList = Stores.composStore.addListener('', () => {
      this.forceUpdate();
    });
  }

  componentWillUnmount() {
    this.disposeComposList();
    this.disposeSingleList();
  }

  render() {
    const singleLines = Stores.singleStore.list();
    const composLines = Stores.composStore.list();

    const content = (singleLines === undefined || composLines === undefined)
      ? this.renderLoading()
      : this.renderPicker(singleLines, composLines);

    const disableSave = this.state.selectedChildren.size === 0;

    return (
      <Modal show={this.props.show} onHide={this.props.onHide}>
        <Modal.Header>
          <h3>Possible lines to add:</h3>
        </Modal.Header>
        <Modal.Body>
            {content}
        </Modal.Body>
        <Modal.Footer>
          <Button className="btn-flex" bsStyle="primary" disabled={disableSave} onClick={this.handleSubmit.bind(this)}>
            <i className="material-icons">add</i> Add
          </Button>
          <Button className="btn-flex" onClick={this.props.onHide}>
            <i className="material-icons">close</i> Close
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }

  renderLoading() {
    // HACK
    return <span>Loading...</span>;
  }

  renderPicker(singleLines, composLines) {
    return (
      <div>
        <h4>Basic lines</h4>
        {this.renderSinglePicker(singleLines)}
        {composLines.length > 0 ? <hr /> : null}
        {composLines.length > 0 ? <h4>Calculated lines</h4> : null}
        {this.maybeRenderComposPicker(composLines)}
      </div>
    );
  }

  renderSinglePicker(singleLines) {
    if (singleLines.length === 0) {
      return <p>You should probably first <a href="/single">add some basic lines</a>!</p>;
    }
    return (
      <ul className="controls">
        {singleLines.map((line, i) => {
          const checked = this.state.selectedChildren.has(line.fullID);
          const labelId = `__sn-${i}`;
          return (
            <li key={labelId}>
              <div className="trow">
                <div className="controlItem">
                  <label htmlFor={labelId}>
                    <input type="checkbox"
                      id={labelId}
                      className="namePickerBox"
                      checked={checked}
                      onChange={this.selectChild.bind(this, line)}
                    />
                    {line.name}
                  </label>
                </div>
              </div>
            </li>
          );
        })}
      </ul>
    );
  }

  maybeRenderComposPicker(composLines) {
    if (composLines.length === 0) {
      return null;
    }
    return (
      <ul className="controls">
        {composLines.map((line, i) => {
          const checked = this.state.selectedChildren.has(line.fullID);
          const labelId = `__cn-${i}`;
          return (
            <li key={labelId}>
              <div className="trow">
                <div className="controlItem">
                  <label htmlFor={labelId}>
                    <input
                      type="checkbox"
                      id={labelId}
                      className="namePickerBox"
                      checked={checked}
                      onChange={this.selectChild.bind(this, line)}
                    />
                    {line.name}
                  </label>
                </div>
              </div>
            </li>
          );
        })}
      </ul>
    );
  }

  selectChild(line, e) {
    const mutableChildren = this.state.selectedChildren;
    if (!!e.target.checked) {
      mutableChildren.add(line.fullID);
    } else {
      mutableChildren.delete(line.fullID);
    }
    this.setState({selectedChildren: mutableChildren});
  }

  handleSubmit() {
    this.props.onPick(Array.from(this.state.selectedChildren));
    this.props.onHide();
    this.setState({selectedChildren: new Set()});
  }
}

export default ChildPickerModal;
