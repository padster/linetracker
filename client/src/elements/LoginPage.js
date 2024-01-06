import React, { Component } from 'react';

const moment = require('moment');

class LoginPage extends Component {
  componentDidMount() {
    window.google.accounts.id.initialize({
      client_id: '1050787416441-9j2g9lvtqllfbsaa1m2dj284hgsldpg8.apps.googleusercontent.com',
      callback: (response) => {
        const credential = response.credential;
        // POST to /_/handle_auth via a form:
        const form = document.createElement('form');
        form.setAttribute('method', 'POST');
        form.setAttribute('action', 'http://localhost:8080/_/handle_auth');
        const hiddenField = document.createElement('input');
        hiddenField.setAttribute('type', 'hidden');
        hiddenField.setAttribute('name', 'credential');
        hiddenField.setAttribute('value', credential);
        form.appendChild(hiddenField);
        const originField = document.createElement('input');
        originField.setAttribute('type', 'hidden');
        originField.setAttribute('name', 'origin');
        const searchParams = new URLSearchParams(window.location.search);
        originField.setAttribute('value', searchParams.get('origin') || '/');
        form.appendChild(originField);
        document.body.appendChild(form);
        form.submit();
      }
    });
    window.google.accounts.id.prompt();
  }

  componentWillUnmount() {
  }

  render() {
    // Google Login
    // https://developers.google.com/identity/gsi/web/guides/migration#redirect-mode
    return (
      <div>
        <div className="g_id_signin" data-type="standard"></div>
      </div>
    );
  }
}

export default LoginPage;
