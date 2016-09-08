import React, { Component } from 'react';

class ComposForm extends Component {
  state: Object;

  componentWillMount() {
    this.state = {
      newLineName: '',
      newLineOp: 'plus',
    };
  }

  render() {
    let btnClass = 'btn';
    if (!this.state.newLineName) {
      btnClass += ' disabled';
    }

    return (
      <div className="formEntry">
        <p>or create a new calculated line: </p>
        <form name="compos" method="post" onSubmit={this.submit.bind(this)}>
          Name = &nbsp;
          <input type="text"
            value={this.state.newLineName}
            onChange={e => this.setState({newLineName: e.target.value})}
          />
          <br/>
          Operation = &nbsp;
          <select
            value={this.state.newLineOp}
            onChange={e => this.setState({newLineOp: e.target.value})}
          >
            <option value="plus">Plus [a+b+c+...]</option>
            <option value="times">Times [a*b*c*...]</option>
            <option value="negate">Negate [0-a-b-c-...]</option>
            <option value="invmult">Inverse [1/(a*b*c*...)]</option>
          </select>
          <br/>
          <input className={btnClass} type="submit" value="Create new calculated line" />
        </form>
      </div>
    );
  }

  submit(e) {
    e.preventDefault();
    this.props.onCreate({
      name: this.state.newLineName,
      op: this.state.newLineOp,
    });
  }
}

export default ComposForm;
