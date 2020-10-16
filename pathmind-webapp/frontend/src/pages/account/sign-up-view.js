import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class SignUpView extends PolymerElement {
  static get template() {
    return html`
    <style include="shared-styles pathmind-dialog-view sign-up-view-styles">
        .inner-content {
            margin-left: var(--lumo-space-m);
            margin-right: var(--lumo-space-m);
        }
        .content-wrapper {
            max-width: 1100px;
            width: 100%;
        }
        .info {
            align-items: flex-start;
            flex: 1 0 60%;
            max-width: 650px;
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
        .info li {
            padding-bottom: var(--lumo-space-s);
        }
        @media screen and (max-width: 768px) {
            .content-wrapper {
                flex-wrap: wrap;
            }
            .inner-content {
                max-width: none;
            }
        }
    </style>
    <vaadin-horizontal-layout class="panel-wrapper">
      <div class="content">
        <span class="welcome-text">Welcome to</span>
        <img
          class="logo"
          src="frontend/images/pathmind-logo.svg"
          alt="Pathmind logo"
        />
        <vaadin-horizontal-layout class="content-wrapper">
            <vaadin-vertical-layout class="inner-content info">
                <div class="video-wrapper">
                    <iframe src="//fast.wistia.net/embed/iframe/py4nssath2" allowtransparency="true" frameborder="0" scrolling="no" class="wistia_embed" name="wistia_embed" allowfullscreen mozallowfullscreen webkitallowfullscreen oallowfullscreen msallowfullscreen></iframe>
                </div>
                <b>Create your free Pathmind account to:</b>
                <ul>
                    <li>Access additional Pathmind-ready simulation models</li>
                    <li>Get tips on how to guide and reward your AI agents</li>
                    <li>Apply AI to your simulation and beat your heuristic</li>
                </ul>
            </vaadin-vertical-layout>
            <vaadin-vertical-layout class="inner-content" id="emailPart">
                <h3>{{title}}</h3>
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
                id="forgotPasswordBtn"
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
        </vaadin-horizontal-layout>
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

  ready() {
    super.ready();
    const script1 = document.createElement("script");
    script1.type = "text/javascript";
    script1.src = "https://fast.wistia.com/embed/medias/py4nssath2.jsonp";

    const script2 = document.createElement("script");
    script2.type = "text/javascript";
    script2.src = "https://fast.wistia.com/assets/external/E-v1.js";

    document.head.appendChild(script1);
    document.head.appendChild(script2);
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
