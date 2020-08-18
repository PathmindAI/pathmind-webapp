import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class SignUpView extends PolymerElement {
  static get template() {
    return html`
    <style include="shared-styles pathmind-dialog-view sign-up-view-styles"></style>
    <vaadin-horizontal-layout class="panel-wrapper">
      <div class="content">
        <span class="welcome-text">Welcome to</span>
        <img
          class="logo"
          src="frontend/images/pathmind-logo.svg"
          alt="Pathmind logo"
        />
        <vaadin-vertical-layout class="inner-content" id="emailPart">
          <h3>{{title}}</h3>
          <div class="do-not-share-url">
            <iron-icon icon="vaadin:exclamation-circle-o"></iron-icon>
            Please do not share this URL
          </div>
          <vaadin-text-field
            id="firstName"
            label="First Name"
          ></vaadin-text-field>
          <vaadin-text-field
            id="lastName"
            label="Last Name"
          ></vaadin-text-field>
          <vaadin-text-field id="email" label="Work Email"></vaadin-text-field>
          <p class="notes" hidden$="{{isEmailUsed}}">
            The email will be used as the User Email during sign in
          </p>
          <vaadin-button
            theme="tertiary small"
            hidden$="{{!isEmailUsed}}"
            onclick="window.location.href='/reset-password'"
            >Want to reset password?</vaadin-button>
          <vaadin-vertical-layout id="buttonsCont">
            <vaadin-button id="signUp" theme="primary">
              Sign up
            </vaadin-button>
          </vaadin-vertical-layout>
          <p class="notes">No credit card required</p>
          <vaadin-button
            theme="tertiary small"
            onclick="window.location.href='/sign-in'"
          >Already have an account?
          </vaadin-button>
        </vaadin-vertical-layout>
        <vaadin-vertical-layout class="inner-content" id="passwordPart">
          <h3>{{title}}</h3>
          <vaadin-password-field
            id="newPassword"
            label="Password"
          ></vaadin-password-field>
          <vaadin-vertical-layout
            id="newPassNotes"
            class="notes"
            style="width: 100%;"
          ></vaadin-vertical-layout>
          <vaadin-password-field
            id="confirmNewPassword"
            label="Confirm Password"
          ></vaadin-password-field>
          <vaadin-vertical-layout id="buttonsCont">
            <vaadin-button id="signIn" theme="primary">
              Get Started!
            </vaadin-button>
            <vaadin-button id="cancelSignInBtn" theme="tertiary">
              Back
            </vaadin-button>
          </vaadin-vertical-layout>
        </vaadin-vertical-layout>
        <div class="policy" id="policyText">
          <span>By clicking Get Started, you agree to Pathmind's </span>
          <a href="https://pathmind.com/subscription-agreement" target="_blank"
            >Terms of Use</a
          ><span> and </span>
          <a href="https://pathmind.com/privacy" target="_blank"
            >Privacy Policy</a
          ><span>.</span>
        </div>
        <a class="support" href="{{contactLink}}">Contact Support</a>
      </div>
    </vaadin-horizontal-layout>`;
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
