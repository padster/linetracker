import React, { Component } from 'react';

class ComposForm extends Component {
  render() {
    return (
      <div className="formEntry">
        <p>or create a new calculated line: </p>
        <form name="compos" method="post" onSubmit={this.submit.bind(this)}>
          Name: <input type="text" name="name" ></input><br/>
          Operation: <select name="op">
          <option value="plus">Plus = a+b+c+...</option>
          <option value="times">Times = a*b*c*...</option>
          <option value="negate">Negate = 0-a-b-c-...</option>
          <option value="invmult">Inverse = 1/(a*b*c*...)</option>
          </select><br/>
          <input className="btn" type="submit" value="Create new calculated line"></input>
        </form>
      </div>
    );
  }

  submit(e) {
    e.preventDefault();
    console.log("Submitting form...");
  }
}

export default ComposForm;
