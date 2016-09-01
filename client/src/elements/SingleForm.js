import React, { Component } from 'react';

class SingleForm extends Component {
  render() {
    return (
      <div className="formEntry">
        <p>or create a new basic line: </p>
        <form name="single" method="post" onSubmit={this.submit.bind(this)}>
        Line name: <input type="text" name="name" ></input>
        <br/><input className="btn" type="submit" value="Create"></input>
        </form>
      </div>
    );
  }

  submit(e) {
    e.preventDefault();
    console.log("Submitting form...");
  }
}

export default SingleForm;
