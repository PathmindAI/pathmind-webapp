import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";
import "@vaadin/vaadin-text-field/src/vaadin-text-field.js";
import "@vaadin/vaadin-text-field/src/vaadin-password-field.js";

// <style include="shared-styles login-view-styles"></style>

class LoginView extends PolymerElement {
  static get template() {
    return html`
      <style include="shared-styles login-view-styles">
      </style>
      <div class="landing-page"></div>
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
        <vaadin-text-field
          label="Label"
          placeholder="my@email.com"
          style="width: 100%;"
        ></vaadin-text-field>
        <vaadin-password-field
          label="Password"
          placeholder="Enter password"
          value="secret1"
          style="width: 100%"
        ></vaadin-password-field>
        <vaadin-button theme="primary" class="sign-in-btn" style="width: 100%;">
          Sign in
        </vaadin-button>
        <vaadin-button theme="tertiary">
          Forgot your password?
        </vaadin-button>
        <div style="flex-shrink: 0;">
          <label style="color: var(--pm-secondary-text-color);"
            >Don't have an account?</label
          >
          <vaadin-button theme="tertiary">
            Get started
          </vaadin-button>
        </div>
      </div>
    `;
  }

  static get is() {
    return "login-view";
  }

  static get properties() {
    return {
      // Declare your properties here.
    };
  }
}

customElements.define(LoginView.is, LoginView);
