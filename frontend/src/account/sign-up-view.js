import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import '@polymer/iron-icon/iron-icon.js';
import '@vaadin/vaadin-icons/vaadin-icons.js';

class SignUpView extends PolymerElement {
  static get template() {
    return html`
<style include="pathmind-dialog-view">
        .content {
          margin: auto;
          max-width: 600px;
          width: 600px;
        }
        #forgotPasswordBtn {
          width: 200px;
        }
        #errorCont {
          justify-content: space-between;
          width: 100%;
        }
        #errorCont .error-message {
          padding-top: 8px;
        }
        .do-not-share-url {
        	display: flex;
        	color: var(--pm-primary-color);
        }
        .do-not-share-url iron-icon {
        	width: 16px;
        	margin-right: 6px;
        }
      </style>
<div class="content">
 <vaadin-tabs>
  <vaadin-tab>
    {{title}} 
  </vaadin-tab>
 </vaadin-tabs>
 <vaadin-vertical-layout style="width: 100%;" class="inner-content" id="emailPart">
  <h3>Sign up for 30 days Free Trial!</h3>
  <div class="do-not-share-url">
    <iron-icon icon="vaadin:exclamation-circle-o"></iron-icon>
    Please do not share this URL 
  </div>
  <div>
    A credit card is not required and the email you provide will be used as the User Email during sign in. 
  </div>
  <vaadin-text-field id="firstName" label="First Name"></vaadin-text-field>
  <vaadin-text-field id="lastName" label="Last Name"></vaadin-text-field>
  <vaadin-text-field id="email" label="Email"></vaadin-text-field>
  <vaadin-horizontal-layout id="errorCont">
   <div class="error-message">
    {{message}}
   </div>
   <vaadin-button id="forgotPasswordBtn" theme="tertiary" hidden$="{{!message}}">
     Want to reset password? 
   </vaadin-button>
  </vaadin-horizontal-layout>
  <vaadin-vertical-layout id="buttonsCont">
   <vaadin-button id="signUp" theme="primary" class="positive-action-btn">
    Sign Up
   </vaadin-button>
   <vaadin-button id="cancelSignUpBtn" theme="tertiary">
    Cancel
   </vaadin-button>
  </vaadin-vertical-layout>
 </vaadin-vertical-layout>
 <vaadin-vertical-layout style="width: 100%;" class="inner-content" id="passwordPart">
  <vaadin-password-field id="newPassword" label="New Password"></vaadin-password-field>
  <vaadin-vertical-layout id="newPassNotes" class="notes" style="width: 100%;"></vaadin-vertical-layout>
  <vaadin-password-field id="confirmNewPassword" label="Confirm New Password"></vaadin-password-field>
  <vaadin-vertical-layout id="buttonsCont">
   <vaadin-button id="signIn" theme="primary" class="positive-action-btn">
    Sign In
   </vaadin-button>
   <vaadin-button id="cancelSignInBtn" theme="tertiary">
    Cancel
   </vaadin-button>
  </vaadin-vertical-layout>
 </vaadin-vertical-layout>
 <a class="support" href="{{contactLink}}">Contact Support</a>
</div>
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
