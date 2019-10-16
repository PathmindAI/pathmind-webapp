import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";
import "@vaadin/vaadin-text-field/src/vaadin-text-field.js";
import "@vaadin/vaadin-text-field/src/vaadin-password-field.js";
import "@polymer/iron-form/iron-form.js";
import "@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js";

import "./landing-page.js";

class LoginView extends PolymerElement {
  static get template() {
    return html`
      <style include="shared-styles login-view-styles">
      </style>
      <vaadin-horizontal-layout style="width: 100%;">
        <landing-page></landing-page>
        <div class="login-panel">
          <label class="welcome-text">Welcome to</label>
          <img
            src="frontend/images/pathmind-logo.png"
            alt="Pathmind logo"
            width="200px"
            class="logo"
          />
          <div class="title">
            <label>Sign in to your new account!</label>
          </div>
          <div part="error-message" hidden$="[[!error]]">
            <h5 part="error-message-title">Incorrect username or password</h5>
          </div>
          <vaadin-vertical-layout id="errorsCont"></vaadin-vertical-layout>
          <iron-form class="login" id="form" allow-redirect>
            <form method="POST" action="login">
              <vaadin-text-field
                id="username"
                name="username"
                label="Email"
                placeholder="Email"
                style="width: 100%;"
                required
                autocapitalize="none"
                autocorrect="off"
                spellcheck="false"
                tabindex="0"
                has-label
                has-value
              ></vaadin-text-field>
              <vaadin-password-field
                id="password"
                name="password"
                label="Password"
                placeholder="Enter password"
                style="width: 100%"
                required
                spellcheck="false"
                tabindex="0"
                has-label
                has-value
              ></vaadin-password-field>
              <vaadin-button
                theme="primary"
                class="sign-in-btn"
                style="width: 100%;"
                id="signIn"
                on-click="login"
              >
                Sign in
              </vaadin-button>
            </form>
          </iron-form>
          <vaadin-button theme="tertiary">
            Forgot your password?
          </vaadin-button>
          <div style="flex-shrink: 0;">
            <label style="color: var(--pm-secondary-text-color);"
              >Don't have an account?</label
            >
            <a href="/sign-up">
              <vaadin-button theme="tertiary">
                Get started
              </vaadin-button>
            </a>
          </div>
        </div>
      </vaadin-horizontal-layout>
    `;
  }

  static get is() {
    return "login-view";
  }

  login() {
    console.warn("login attemt");
    if (!this.$.username.invalid && !this.$.password.invalid) {
      console.warn("login attemt: go");
      this.$.form.submit();
    }
  }

  static get properties() {
    return {
      // Declare your properties here.
    };
  }
}

customElements.define(LoginView.is, LoginView);
