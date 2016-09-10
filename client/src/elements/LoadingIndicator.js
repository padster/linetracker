import React, { Component } from 'react';

class LoadingIndicator extends Component {
  render() {
    return (
      <div className='loading'>
        <div className='spinner'>
          <div className='rect1' />
          <div className='rect2' />
          <div className='rect3' />
          <div className='rect4' />
          <div className='rect5' />
        </div>
      </div>
    );
  }
}

export default LoadingIndicator;
