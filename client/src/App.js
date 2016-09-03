import React, { Component } from 'react';
import './App.css';

import AllInputsList from './elements/AllInputsList.js';
import ComposItem from './elements/ComposItem.js';
import ComposList from './elements/ComposList.js';
import GraphsItem from './elements/GraphsItem.js';
import GraphsList from './elements/GraphsList.js';
import HomePage from './elements/HomePage.js';
import SingleItem from './elements/SingleItem.js';
import SingleList from './elements/SingleList.js';

class App extends Component {
  render() {
    return (
      <div>
        <div className="container">
          <div className="navbar">
            <div className="navbar-inner">
              <span className="brand">Line Tracking</span>
              <ul className="nav">
                <li><a href="/">Home</a></li>
                <li><a href="/graphs">Graphs</a></li>
                <li><a href="/compos">Calculated</a></li>
                <li><a href="/single">Basic</a></li>
                <li><a href="/allInputs">Bulk entry</a></li>
                <li><a href="/pages/help.html">FAQ</a></li>
            </ul>
            </div>
          </div>

          <div id='root'>
            {this.renderContent()}
          </div>
        </div>
        {/* TODO: Loading indicator
        <div id='loading'>
          <div className='spinner'>
            <div className='rect1'></div>
            <div className='rect2'></div>
            <div className='rect3'></div>
            <div className='rect4'></div>
            <div className='rect5'></div>
          </div>
        </div>
        */}
      </div>
    );
  }

  renderContent() {
    const pathParts = window.location.pathname.split("/").splice(1);
    if (pathParts.length === 1) {
      switch (pathParts[0]) {
        case 'single':
          return <SingleList />;
        case 'compos':
          return <ComposList />;
        case 'graphs':
          return <GraphsList />;
        case 'allInputs':
          return <AllInputsList />;
        case '': // Home
          return <HomePage />;
        default: // no-op
      }
      return "Oops, not known";
    } else if (pathParts.length === 2) {
      switch (pathParts[0]) {
        case 'single':
          return <SingleItem id={pathParts[1]} />;
        case 'compos':
          return <ComposItem id={pathParts[1]} />;
        case 'graphs':
          return <GraphsItem id={pathParts[1]} />;
        default: // no-op
      }
    }
    return "Hmm, need to render";
  }
}

export default App;
