import React, { Component } from 'react';
import './App.css';

import AllInputsList from './elements/AllInputsList.js';
import ComposChart from './elements/ComposChart.js';
import ComposItem from './elements/ComposItem.js';
import ComposList from './elements/ComposList.js';
import GraphsChart from './elements/GraphsChart.js';
import GraphsItem from './elements/GraphsItem.js';
import GraphsList from './elements/GraphsList.js';
import HomePage from './elements/HomePage.js';
import SingleChart from './elements/SingleChart.js';
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
    } else if (pathParts.length === 3) {
      if (pathParts[0] === "view") {
        switch (pathParts[1]) {
          case 'single':
            return <SingleChart id={pathParts[2]} />;
          case 'compos':
            return <ComposChart id={pathParts[2]} />;
          case 'graphs':
            return <GraphsChart id={pathParts[2]} />;
          default: // no-op
        }
      }
    }
    return "Hmm, need to render";
  }
}

export default App;
