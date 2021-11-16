import { LitElement, html, property } from "lit-element";
import "../../components/organisms/public-header-menu.ts";

class SignUpView extends LitElement {
    @property({type: String})
    contactLink = "";
    @property({type: Boolean})
    hasCreatedPassword = false;
    @property({type: Boolean})
    isEmailUsed = false;

  render() {
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
    <public-header-menu contactlink="${this.contactLink}"></public-header-menu>
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
          </vaadin-vertical-layout>
      </div>
    </vaadin-horizontal-layout>`;
  }

  createRenderRoot() {
    return this;
  }

  firstUpdated() {
    (window as any).detectIntercomAndFireTrackEventWhenLoaded = this.detectIntercomAndFireTrackEventWhenLoaded;

    (window as any).detectIntercomAndFireTrackEventWhenLoaded();
  }

  detectIntercomAndFireTrackEventWhenLoaded() {
    if (typeof Intercom === "undefined") {
        requestAnimationFrame((window as any).detectIntercomAndFireTrackEventWhenLoaded);
    } else {
        Intercom('trackEvent', 'Sign Up Page Visited');
    }
  }

  onCreatePasswordInput(event) {
    const passwordValue = event.target.value;
    this.hasCreatedPassword = (passwordValue.length > 0);
  }
}

customElements.define("sign-up-view", SignUpView);
