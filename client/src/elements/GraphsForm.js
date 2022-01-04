import React, { Component } from 'react';

class GraphsForm extends Component {
  state: Object;

  constructor(props) {
    super(props);
    this.state = {
      newLineName: '',
    };
  }

  render() {
    let btnClass = 'btn';
    if (!this.state.newLineName) {
      btnClass += ' disabled';
    }

    return (
      <div className="formEntry">
        <p>or create a new graph: </p>
        <form name="graphs" method="post" onSubmit={this.submit.bind(this)}>
        Name = &nbsp;
        <input type="text"
          value={this.state.newLineName}
          onChange={e => this.setState({newLineName: e.target.value})}
        />
        <br />
        <input className={btnClass} type="submit" value="Create new graph" />
        </form>
      </div>
    );
  }

  submit(e) {
    e.preventDefault();
    this.props.onCreate({
      name: this.state.newLineName,
    });
  }
}

export default GraphsForm;
