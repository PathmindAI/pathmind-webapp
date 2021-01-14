import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class AccountUpgradeViewContent extends PolymerElement {
  static get template() {
    return html`
      <style include="shared-styles pathmind-dialog-view">
        account-upgrade-view-content {
            box-sizing: border-box;
            width: 100%;
            justify-content: center;
            padding: var(--lumo-space-xl) var(--lumo-space-m);
            text-align: center;
        }
        account-upgrade-view-content > vaadin-horizontal-layout {
            justify-content: center;
            align-items: stretch;
            width: 100%;
        }
        account-upgrade-view-content h1 {
            margin: 0 auto var(--lumo-space-xl);
        }
        account-upgrade-view-content .inner-content {
            flex: 1 0 calc((100% - var(--lumo-space-l) * 2) / 3);
          width: calc((100% - var(--lumo-space-l) * 2) / 3);
          font-size: var(--lumo-font-size-m);
          padding: var(--lumo-space-l);
          border: 1px solid var(--pm-grey-color-lighter);
          margin-top: 0;
        }
        account-upgrade-view-content .inner-content > :last-child {
            width: 100%;
            margin: auto auto var(--lumo-space-m);
        }
        account-upgrade-view-content .inner-content:nth-child(2) {
          margin: 0 var(--lumo-space-l) 0;
        }
        @media screen and (max-width: 1023px) {
            account-upgrade-view-content {
                height: auto;
                padding: var(--lumo-space-m) var(--lumo-space-l) var(--lumo-space-l);
            }
            account-upgrade-view-content > vaadin-horizontal-layout {
                flex-direction: column;
            }
            account-upgrade-view-content .inner-content {
                flex: 1 1 auto;
                max-width: 100%;
                width: 100%;
                margin: 0;
            }
            account-upgrade-view-content .inner-content:nth-child(2) {
                margin: var(--lumo-space-xxl) 0;
            }
        }
        account-upgrade-view-content .card-header {
            position: relative;
            align-items: center;
            width: calc(100% + 2 * var(--lumo-space-l));
            padding: var(--lumo-space-l) var(--lumo-space-l);
            margin: calc(-1 * var(--lumo-space-l)) calc(-1 * var(--lumo-space-l)) 0;
            border-radius: var(--lumo-border-radius);
        }
        account-upgrade-view-content .popular-tag {
            position: absolute;
            top: calc(-1 * var(--lumo-space-s));
            right: var(--lumo-space-s);
            color: white;
            font-size: var(--lumo-font-size-xs);
            font-weight: bold;
            line-height: 1;
            letter-spacing: .04rem;
            background-color: var(--pm-friendly-color);
            padding: var(--lumo-space-xxs) var(--lumo-space-xs);
            border-radius: var(--lumo-border-radius-s);
            box-shadow: 0px 2px 3px rgba(0, 0, 0, 0.16);
        }
        account-upgrade-view-content .inner-content:nth-child(1) .card-header {
            background-color: var(--pm-grey-color-lightest);
        }
        account-upgrade-view-content .inner-content:nth-child(1) .title {
            color: var(--pm-grey-color-darkest);
        }
        account-upgrade-view-content .inner-content:nth-child(2) .card-header {
            background-color: rgb(216 238 245);
        }
        account-upgrade-view-content .inner-content:nth-child(2) .title {
            color: var(--pm-primary-color-dark);
        }
        account-upgrade-view-content .inner-content:nth-child(3) .card-header {
            background-color: var(--pm-blue-color-light);
        }
        account-upgrade-view-content .inner-content:nth-child(3) .title {
            color: var(--pm-blue-color-dark);
        }
        account-upgrade-view-content .title {
            letter-spacing: .05rem;
            margin: 0 auto;
        }
        account-upgrade-view-content .price-cont {
            display: flex;
            flex-direction: column;
            align-items: center;
            font-size: var(--lumo-font-size-xl);
            line-height: 1.3;
          margin: var(--lumo-space-l) auto;
        }
        account-upgrade-view-content .price {
            font-size: 4rem;
            font-family: var(--lumo-font-family-header);
            font-weight: 500;
            letter-spacing: -.03rem;
            margin-right: var(--lumo-space-xxxs);
        }
        account-upgrade-view-content .additional-info {
            font-size: var(--lumo-font-size-s);
        }
        account-upgrade-view-content .features {
            text-align: left;
            padding: 0;
            margin: var(--lumo-space-m) 0 var(--lumo-space-xl) var(--lumo-space-l);
        }
        account-upgrade-view-content li {
            list-style: none;
            position: relative;
            padding-left: var(--lumo-font-size-xl);
            margin-bottom: var(--lumo-space-s);
        }
        account-upgrade-view-content li::before {
            content: '';
            display: block;
            position: absolute;
            top: calc(var(--lumo-font-size-s) / 2);
            left: 0;
            width: var(--lumo-space-s);
            height: var(--lumo-space-s);
            background-color: var(--lumo-primary-color);
        }
        account-upgrade-view-content vaadin-button {
            width: 100%;
            margin: 0;
        }
        account-upgrade-view-content vaadin-button[disabled] {
            color: var(--lumo-secondary-text-color);
            font-weight: bold;
        }
        account-upgrade-view-content a {
          margin: auto;
        }
        account-upgrade-view-content .caption {
            font-size: var(--lumo-font-size-s);
          color: var(--lumo-secondary-text-color);
          margin-top: var(--lumo-space-xl);
        }
      </style>
    <h1>Subscription Plans</h1>
    <vaadin-horizontal-layout>
        <vaadin-vertical-layout class="inner-content">
            <vaadin-vertical-layout class="card-header">
                <h2 class="title">Basic</h2>
                <span class="details">For Students and Hobbyists</span>
            </vaadin-vertical-layout>
            <span class="price-cont">
                <span class="price">Free</span>
                <span class="additional-info">&nbsp;</span>
            </span>
            <ul class="features">
                <li>25 Experiments Per Month</li>
                <li>Unlimited Policy Export</li>
            </ul>
            <vaadin-button id="freeBtn" theme="tertiary" disabled>Current Plan</vaadin-button>
        </vaadin-vertical-layout>
        <vaadin-vertical-layout class="inner-content">
            <vaadin-vertical-layout class="card-header">
                <h2 class="title">Professional</h2>
                <span class="details">For Professional Simulation Engineers</span>
                <span class="popular-tag">POPULAR</span>
            </vaadin-vertical-layout>
            <span class="price-cont">
                <span><span class="price">$500</span>/Month</span>
                <span class="additional-info">For yearly subscription per seat</span>
            </span>
            <ul class="features">
                <li>200 Experiments Per Month</li>
                <li>Unlimited Policy Export</li>
                <li>Technical Support Included</li>
            </ul>
            <vaadin-button id="proBtn" theme="primary">Choose Pro</vaadin-button>
        </vaadin-vertical-layout>
        <vaadin-vertical-layout class="inner-content">
            <vaadin-vertical-layout class="card-header">
                <h2 class="title">Enterprise</h2>
                <span class="details">For Consultancies & Corporate Teams</span>
            </vaadin-vertical-layout>
            <span class="price-cont">
                <span><span class="price">$1,000</span>/Month</span>
                <span class="additional-info">For yearly subscription per seat</span>
            </span>
            <ul class="features">
                <li>2,000 Experiments Per Month</li>
                <li>Unlimited Policy Export</li>
                <li>Technical Support Included</li>
                <li>Policy Serving Enabled</li>
                <li>RL Advisory and Training</li>
            </ul>
            <a href="{{contactLink}}">
                <vaadin-button id="enterpriseBtn" theme="primary">Contact Us</vaadin-button>
            </a>
        </vaadin-vertical-layout>
    </vaadin-horizontal-layout>
    <p class="caption">Applicable taxes not included</p>
    `;
  }

  _attachDom(dom) {
    this.appendChild(dom);
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
