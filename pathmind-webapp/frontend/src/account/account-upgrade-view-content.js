import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js";

class AccountUpgradeViewContent extends PolymerElement {
  static get template() {
    return html`
      <style include="shared-styles pathmind-dialog-view">
        :host {
          display: block;
          height: 100%;
        }

        .content {
          max-width: 100%;
          padding: 60px 0;
        }

        .cards-holder {
          justify-content: center;
          width: 100%;
          height: 100%;
        }

        .inner-content {
          margin: 10px 0px;
          width: 400px;
          border-top: 8px solid var(--pm-primary-color);
        }

        .professional-cont {
          margin: 0 6px;
        }

        .card-header {
          width: 250px;
          margin: auto;
          padding-bottom: 10px;
          border-bottom: 1px solid var(--pm-primary-color);
          align-items: center;
        }

        .title {
          font-size: 1.5em;
          font-weight: 300;
        }

        .details {
          font-size: 0.8em;
          color: #676767;
          font-weight: 500;
        }

        .price-cont {
          margin: 20px auto;
          align-items: center;
          display: flex;
        }

        .price {
          font-size: 2em;
        }

        .pre-part {
          padding-bottom: 8px;
          padding-right: 4px;
        }

        .post-part {
          padding-left: 5px;
          color: #676767;
        }

        .feature {
          padding: 5px 0 5px 25px;
          color: #878787;
          font-size: 0.85em;
        }

        .feature-available {
          padding-left: 0px;
          color: var(--pm-text-color);
          align-self: center;
          font-weight: bold;
        }

        .highlight {
          font-weight: 700;
        }

        iron-icon {
          width: 16px;
          height: 16px;
          color: var(--pm-friendly-color);
          padding-right: 5px;
        }

        vaadin-button {
          margin: 35px auto 25px;
        }
        a {
          margin: auto;
        }

        .support {
          color: #878787;
          padding-top: 40px;
        }
      </style>
      <div class="content">
        <vaadin-horizontal-layout class="cards-holder">
          <vaadin-vertical-layout class="inner-content">
            <vaadin-vertical-layout class="card-header">
              <span class="title">Trial</span>
              <span class="details">30 Days Trial</span>
            </vaadin-vertical-layout>
            <span class="price-cont">
              <span class="price">Free</span>
            </span>

            <div class="feature feature-available">
              Details coming soon.
            </div>

            <vaadin-button id="freeBtn" theme="tertiary" disabled
              >Current Plan</vaadin-button
            >
          </vaadin-vertical-layout>

          <vaadin-vertical-layout class="inner-content professional-cont">
            <vaadin-vertical-layout class="card-header">
              <span class="title">Professional</span>
              <span class="details">Monthly Subscription</span>
            </vaadin-vertical-layout>
            <div class="feature feature-available">
              Details coming soon.
            </div>

            <vaadin-button
              id="proBtn"
              theme="primary"
              class="positive-action-btn"
              >Choose Pro</vaadin-button
            >
          </vaadin-vertical-layout>

          <vaadin-vertical-layout class="inner-content">
            <vaadin-vertical-layout class="card-header">
              <span class="title">Enterprise</span>
              <span class="details">Annual Subscription</span>
            </vaadin-vertical-layout>
            <span class="price-cont">
              <span class="post-part">Contact Pathmind for more details</span>
            </span>

            <div class="feature feature-available">
              Details coming soon.
            </div>

            <a href="{{contactLink}}">
              <vaadin-button
                id="enterpriseBtn"
                theme="primary"
                class="positive-action-btn"
                >Contact Us</vaadin-button
              >
            </a>
          </vaadin-vertical-layout>
        </vaadin-horizontal-layout>
        <div class="support">Applicable taxes not included</div>
      </div>
    `;
  }

  static get is() {
    return "account-upgrade-view-content";
  }

  static get properties() {
    return {
      // Declare your properties here.
    };
  }
}

customElements.define(AccountUpgradeViewContent.is, AccountUpgradeViewContent);
