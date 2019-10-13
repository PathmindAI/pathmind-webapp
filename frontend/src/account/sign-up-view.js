import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

/**
 * `sign-up-view`
 *
 * SignUpView element.
 *
 * @customElement
 * @polymer
 */
class SignUpView extends PolymerElement {
  static get template() {
    return html`
      <style include="shared-styles">
        :host {
          display: block;
          height: 100%;
          width: 600px;
          margin: 50px auto 0;
          color: #696969;
        }

        #content {
          margin-top: 5px;
          background: #fff;
          border: 1px solid #ccc;
          border-radius: 0.3em;
          padding: 50px 80px 30px;
        }

        vaadin-text-field {
          width: 100%;
        }

        #buttonsCont {
          margin-top: 60px;
          width: 100%;
        }

        vaadin-button {
          margin: 5px auto;
        }
        #updateBtn {
          width: 250px;
        }
      </style>
      <vaadin-tabs>
        <vaadin-tab>
          Get Started!
        </vaadin-tab>
      </vaadin-tabs>
      <vaadin-vertical-layout style="width: 100%;" id="content">
        <h3>Sign up for 30 days Free Trial!</h3>
        <div>A credit card is not required and the email you provide will be used as the User Email during sign in.</div>

        <!-- <vaadin-vertical-layout style="width: 100%; height: 100%;"> -->
          <vaadin-text-field
            id="firstName"
            label="First Name"
          ></vaadin-text-field>
          <vaadin-text-field
            id="lastName"
            label="Last Name"
          ></vaadin-text-field>
          <vaadin-text-field id="email" label="Email"></vaadin-text-field>
          <vaadin-vertical-layout id="buttonsCont">
            <vaadin-button id="updateBtn" theme="primary">Sign Up</vaadin-button>
            <vaadin-button id="cancelBtn" theme="tertiary">Cancel</vaadin-button>
            
          <!-- </vaadin-vertical-layout> -->
        </vaadin-vertical-layout>
      </vaadin-vertical-layout>
    `;
  }

  static get is() {
    return "sign-up-view";
  }

  static get properties() {
    return {
      // Declare your properties here.
    };
  }
}

customElements.define(SignUpView.is, SignUpView);
