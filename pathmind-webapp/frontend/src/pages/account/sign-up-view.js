import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "../../components/organisms/public-header-menu.js";

class SignUpView extends PolymerElement {
  static get template() {
    return html`
    <style>
        body {
            background-color: var(--pm-app-bg-color);
        }
        sign-up-view {
          --lumo-primary-color: var(--pm-primary-color-bright);
          height: auto;
          min-height: 100%;
        }
        sign-up-view .inner-content {
          align-items: center;
          max-width: 460px;
          margin-top: 0;
          margin-left: var(--lumo-space-m);
        }
        sign-up-view .content-wrapper {
          justify-content: center;
          width: 100%;
        }
        sign-up-view .content {
          box-sizing: border-box;
          width: 100%;
          padding: 6rem var(--lumo-space-m);
        }
        sign-up-view .info {
          align-items: flex-start;
          flex: 1 1 60%;
          max-width: 600px;
          font-size: var(--lumo-font-size-l);
          padding-top: var(--lumo-space-xl);
        }
        sign-up-view .video-wrapper {
          position: relative;
          width: 100%;
          padding-bottom: 56.39%;
          margin-bottom: var(--lumo-space-xl);
        }
        sign-up-view .video-wrapper iframe {
          position: absolute;
          width: 100%;
          height: 100%;
          top: 0;
          left: 0;
        }
        sign-up-view .info h1 {
          color: var(--lumo-primary-color);
          line-height: 1.5;
          margin: 0;
        }
        sign-up-view .info p {
          margin: var(--lumo-space-xl) 0 var(--lumo-space-m);
        }
        sign-up-view .info ul {
          --lumo-primary-color: var(--pm-primary-color);
          padding: 0 0 0 var(--lumo-font-size-xxl);
          margin-top: 0;
        }
        sign-up-view .info li {
          list-style: none;
          position: relative;
          padding-left: var(--lumo-font-size-xl);
          margin-bottom: var(--lumo-space-s);
        }
        sign-up-view .info li::before {
          content: '';
          display: block;
          position: absolute;
          top: calc(var(--lumo-font-size-l) / 2);
          left: 0;
          width: var(--lumo-space-s);
          height: var(--lumo-space-s);
          background-color: var(--lumo-primary-color);
        }
        sign-up-view .info li::marker {
          display: none;
        }
        sign-up-view h3 {
          font-weight: 600;
          margin-top: var(--lumo-space-xs);
        }
        sign-up-view .names-wrapper {
          justify-content: space-between;
          width: 100%;
        }
        sign-up-view .names-wrapper vaadin-text-field {
          width: calc(50% - var(--lumo-space-xs));
        }
        sign-up-view .passwords-wrapper {
          width: 100%;
        }
        sign-up-view vaadin-text-field {
            text-align: left;
        }
        sign-up-view .policy a {
          color: var(--pm-primary-color);
        }
        @media screen and (max-width: 768px) {
            sign-up-view .content {
              padding: var(--lumo-space-xxl) var(--lumo-space-s);
            }
            sign-up-view .content-wrapper {
                flex-wrap: wrap;
            }
            sign-up-view .inner-content {
                max-width: none;
                margin-left: 0;
            }
            sign-up-view .info {
              font-size: var(--lumo-font-size-l);
              padding: 0 var(--lumo-space-s);
            }
            sign-up-view .names-wrapper {
              flex-direction: column;
            }
            sign-up-view .names-wrapper vaadin-text-field {
              width: 100%;
            }
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
                  <li>Get tips on how-to guide and reward your AI agents</li>
                  <li>Apply AI to your simulation and beat your heuristic</li>
              </ul>
              <!--<div class="video-wrapper">
                  <iframe src="//fast.wistia.net/embed/iframe/py4nssath2" allowtransparency="true" frameborder="0" scrolling="no" class="wistia_embed" name="wistia_embed" allowfullscreen mozallowfullscreen webkitallowfullscreen oallowfullscreen msallowfullscreen></iframe>
              </div>-->
          </vaadin-vertical-layout>
          <vaadin-vertical-layout class="inner-content" id="emailPart">
              <h3>Make Better Decisions With AI</h3>
              <vaadin-horizontal-layout class="names-wrapper">
                <vaadin-text-field
                  id="firstName"
                  label="First Name"
                  ></vaadin-text-field>
                <vaadin-text-field
                  id="lastName"
                  label="Last Name"
                  ></vaadin-text-field>
              </vaadin-horizontal-layout>
              <vaadin-text-field id="email" label="Work Email"></vaadin-text-field>
              <p class="notes" hidden$="{{isEmailUsed}}">
              The email will be used as the User Email during sign in
              </p>
              <vaadin-vertical-layout
                class="passwords-wrapper"
                hidden$="{{isEmailUsed}}">
                <vaadin-password-field
                    id="newPassword"
                    label="Create Password"
                    on-keyup="onCreatePasswordInput"
                ></vaadin-password-field>
                <vaadin-vertical-layout
                    id="newPassNotes"
                    class="notes"
                    style="width: 100%;"
                ></vaadin-vertical-layout>
                <vaadin-password-field
                    id="confirmNewPassword"
                    label="Confirm Password"
                    hidden$="{{!hasCreatedPassword}}"
                ></vaadin-password-field>
              </vaadin-vertical-layout>
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
                <span>By submitting the form, you agree to our </span>
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

  _attachDom(dom) {
    this.appendChild(dom);
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
        Intercom('trackEvent', 'Sign Up Page Visited');
    }
  }

  onCreatePasswordInput(event) {
    const passwordValue = event.target.value;
    this.hasCreatedPassword = (passwordValue.length > 0);
  }

  static get is() {
    return "sign-up-view";
  }

  static get properties() {
    return {
      hasCreatedPassword: {
        type: Boolean,
        value: false,
      },
    };
  }
}

customElements.define(SignUpView.is, SignUpView);
