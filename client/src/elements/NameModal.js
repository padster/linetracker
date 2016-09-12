import React, { Component } from 'react';
import { Button, Modal } from 'react-bootstrap';

/** UI to show a modal popup for editing a line's name. */
class NameModal extends Component {
  state: Object;

  componentWillMount() {
    this.state = {
      nameOverride: null,
    };
  }

  render() {
    const disableSave =
      (this.state.nameOverride === null || this.state.nameOverride === '');

    const inputValue = this.state.nameOverride === null
      ? this.props.defaultValue
      : this.state.nameOverride;
    const changeValue = e => this.setState({
      nameOverride: e.target.value !== this.props.defaultValue ? e.target.value : null,
    });

    return (
      <Modal show={this.props.show} onHide={this.props.onHide}>
        <Modal.Header>
          <h3>New name:</h3>
        </Modal.Header>
        <Modal.Body>
            <input type="text" value={inputValue} onChange={changeValue}></input>
        </Modal.Body>
        <Modal.Footer>
          <Button className="btn-flex" bsStyle="primary" disabled={disableSave} onClick={this.handleSubmit.bind(this)}>
            <i className="material-icons">add</i> Change
          </Button>
          <Button className="btn-flex" onClick={this.props.onHide}>
            <i className="material-icons">close</i> Cancel
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }

  handleSubmit() {
    this.props.onChange(this.state.nameOverride);
    this.props.onHide();
    this.setState({nameOverride: null});
  }
}

export default NameModal;
