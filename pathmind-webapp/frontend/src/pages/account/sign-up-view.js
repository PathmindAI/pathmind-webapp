import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "../../components/organisms/public-header-menu.js";

class SignUpView extends PolymerElement {
  static get template() {
    return html`
    <style include="shared-styles pathmind-dialog-view sign-up-view-styles">
        .inner-content {
            margin-top: 0;
            margin-left: var(--lumo-space-m);
        }
        .content-wrapper {
            justify-content: center;
            width: 100%;
        }
        .content {
          box-sizing: border-box;
          width: 100%;
          padding: 6rem var(--lumo-space-xl);
        }
        .info {
            align-items: flex-start;
            flex: 1 0 60%;
            max-width: 600px;
            font-size: var(--lumo-font-size-l);
            padding-top: var(--lumo-space-xl);
        }
        .video-wrapper {
            position: relative;
            width: 100%;
            padding-bottom: 56.39%;
            margin-bottom: var(--lumo-space-xl);
        }
        .video-wrapper iframe {
            position: absolute;
            width: 100%;
            height: 100%;
            top: 0;
            left: 0;
        }
        .info h1 {
          color: var(--lumo-primary-color);
          line-height: 1.5;
          margin: 0;
        }
        .info p {
          margin: var(--lumo-space-xl) 0 var(--lumo-space-m);
        }
        .info ul {
          padding: 0 0 0 var(--lumo-font-size-xxl);
          margin-top: 0;
        }
        .info li {
          list-style: none;
          position: relative;
          padding-left: var(--lumo-font-size-xl);
          margin-bottom: var(--lumo-space-s);
        }
        .info li::before {
          content: '';
          display: block;
          position: absolute;
          top: calc(var(--lumo-font-size-l) / 2);
          left: 0;
          width: var(--lumo-space-s);
          height: var(--lumo-space-s);
          background-color: var(--lumo-primary-color);
        }
        .info li::marker {
          display: none;
        }
        h3 {
          margin-top: var(--lumo-space-xs);
        }
        @media screen and (max-width: 768px) {
            :host {
              height: auto;
              min-height: 100%;
            }
            .content {
              padding: var(--lumo-space-xxl) var(--lumo-space-s);
            }
            .content-wrapper {
                flex-wrap: wrap;
            }
            .inner-content {
                max-width: none;
                margin-left: 0;
            }
            .info {
              font-size: var(--lumo-font-size-l);
              padding: 0 var(--lumo-space-s);
            }
        }
        #firstName, #lastName {
          display: none;
        }
    </style>
    <public-header-menu contactlink="{{contactLink}}"></public-header-menu>
      <div class="content">
        <vaadin-horizontal-layout class="content-wrapper">
          <vaadin-vertical-layout class="info">
              <h1>
                Optimize your simulation with AI.<br/>
                Quick, simple, and effective.
              </h1>
              <p>Get your free Pathmind account to:</p>
              <ul>
                  <li>Access additional AI-ready simulation models</li>
                  <li>Get tips on how to guide and reward your AI agents</li>
                  <li>Apply AI to your simulation and beat your heuristic</li>
              </ul>
              <!--<div class="video-wrapper">
                  <iframe src="//fast.wistia.net/embed/iframe/py4nssath2" allowtransparency="true" frameborder="0" scrolling="no" class="wistia_embed" name="wistia_embed" allowfullscreen mozallowfullscreen webkitallowfullscreen oallowfullscreen msallowfullscreen></iframe>
              </div>-->
          </vaadin-vertical-layout>
          <vaadin-vertical-layout class="inner-content" id="emailPart">
              <h3>Make Better Decisions With AI</h3>
              <vaadin-text-field
                id="firstName"
                label="First Name"
                value=""
                ></vaadin-text-field>
              <vaadin-text-field
                id="lastName"
                label="Last Name"
                value=""
                ></vaadin-text-field>
              <vaadin-text-field id="email" label="Work Email"></vaadin-text-field>
              <p class="notes" hidden$="{{isEmailUsed}}">
              The email will be used as the User Email during sign in
              </p>
              <vaadin-password-field
                  id="newPassword"
                  label="Create Password"
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
              <vaadin-button
                id="forgotPasswordBtn"
                theme="tertiary small"
                hidden$="{{!isEmailUsed}}"
                onclick="window.location.href='/reset-password'"
              >Want to reset password?</vaadin-button>
              <vaadin-vertical-layout id="buttonsCont">
                <vaadin-button id="signIn" theme="primary">
                  Create Free Account
                </vaadin-button>
              </vaadin-vertical-layout>
              <vaadin-button
                id="alreadyHaveAccount"
                theme="tertiary small"
                onclick="window.location.href='/sign-in'"
                >Already have an account?
              </vaadin-button>
              <div class="policy" id="policyText">
                <span>By submitting the form, you agree to Pathmind's </span>
                <a href="https://pathmind.com/subscription-agreement" target="_blank"
                  >Terms of Use</a
                ><span> and </span>
                <a href="https://pathmind.com/privacy" target="_blank"
                  >Privacy Policy</a
                ><span>.</span>
              </div>
          </vaadin-vertical-layout>
      </div>
    </vaadin-horizontal-layout>`;
  }

  ready() {
    super.ready();

    window.detectIntercomAndFireTrackEventWhenLoaded = this.detectIntercomAndFireTrackEventWhenLoaded;

    detectIntercomAndFireTrackEventWhenLoaded();
  }

  detectIntercomAndFireTrackEventWhenLoaded() {
    if (typeof Intercom === "undefined") {
        requestAnimationFrame(detectIntercomAndFireTrackEventWhenLoaded);
    } else {
        Intercom('trackEvent', 'Visited Sign Up Page');
    }
  }

  static get is() {
    return "sign-up-view";
  }

  static get properties() {
    return {
    };
  }
}

customElements.define(SignUpView.is, SignUpView);
