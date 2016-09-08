import React, { Component } from 'react';

class SingleForm extends Component {
  state: Object;

  componentWillMount() {
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
        <p>or create a new basic line: </p>
        <form name="single" method="post" onSubmit={this.submit.bind(this)}>
        Line name = &nbsp;
        <input type="text"
          value={this.state.newLineName}
          onChange={e => this.setState({newLineName: e.target.value})}
        />
        <br/>
        <input className={btnClass} type="submit" value="Create new basic line" />
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

export default SingleForm;
