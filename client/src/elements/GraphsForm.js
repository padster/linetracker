import React, { Component } from 'react';

class GraphsForm extends Component {
  render() {
    return (
      <div className="formEntry">
        <p>or create a new graph: </p>
        <form name="graphs" method="post" onSubmit={this.submit.bind(this)}>
        Name: <input type="text" name="name" ></input><br />
        <input className="btn" type="submit" value="Create new graph"></input>
        </form>
      </div>
    );
  }

  submit(e) {
    e.preventDefault();
    console.log("Submitting form...");
  }
}

export default GraphsForm;
