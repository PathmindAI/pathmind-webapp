import { LitElement, html, property } from "lit-element";

class AccountUpgradeViewContent extends LitElement {
  @property({type: String})
  apiUrl = "";
  @property({type: String})
  contactLink = "";
  @property({type: String})
  key = "";
  @property({type: Object})
  stripeObj;
  @property({type: String})
  userApiKey = "";
  @property({type: Boolean})
  isTrialUser = true;

  render() {
    return html`
      <style>
        account-upgrade-view-content {
            box-sizing: border-box;
            width: 100%;
            justify-content: center;
            background: white;
            padding: var(--lumo-space-xl) var(--lumo-space-xxl);
            text-align: center;
        }
        account-upgrade-view-content > vaadin-horizontal-layout {
            justify-content: center;
            align-items: stretch;
            width: 100%;
            padding: 0 var(--lumo-space-m);
        }
        account-upgrade-view-content .header {
            margin: 0 auto var(--lumo-space-xl);
        }
        account-upgrade-view-content .header h1 {
            margin: 0 auto var(--lumo-space-xxs);
        }
        account-upgrade-view-content .inner-content {
          flex: 1 0 calc((100% - var(--lumo-space-m) * 3) / 4);
          max-width: 310px;
          width: calc((100% - var(--lumo-space-m) * 3) / 4);
          font-size: var(--lumo-font-size-m);
          padding: var(--lumo-space-m);
          margin-top: 0;
        }
        account-upgrade-view-content .inner-content > a,
        account-upgrade-view-content .inner-content > vaadin-button {
            width: 100%;
            margin: auto auto var(--lumo-space-m);
        }
        account-upgrade-view-content .inner-content:nth-child(2),
        account-upgrade-view-content .inner-content:nth-child(3),
        account-upgrade-view-content .inner-content:nth-child(4) {
          margin: 0 0 0 var(--lumo-space-m);
        }
        @media screen and (max-width: 1023px) {
            account-upgrade-view-content {
                height: auto;
                padding: var(--lumo-space-m) var(--lumo-space-l) var(--lumo-space-l);
            }
            account-upgrade-view-content > vaadin-horizontal-layout {
                flex-wrap: wrap;
            }
            account-upgrade-view-content .inner-content {
                flex: 1 0 280px;
                max-width: 100%;
                width: 100%;
                margin: 0;
            }
            account-upgrade-view-content .inner-content:nth-child(1),
            account-upgrade-view-content .inner-content:nth-child(2),
            account-upgrade-view-content .inner-content:nth-child(3),
            account-upgrade-view-content .inner-content:nth-child(4) {
                margin: var(--lumo-space-m);
            }
        }
        account-upgrade-view-content .card-header {
            position: relative;
            align-items: center;
            width: calc(100% + 2 * var(--lumo-space-m));
            padding: var(--lumo-space-m) var(--lumo-space-m);
            margin: calc(-1 * var(--lumo-space-m)) calc(-1 * var(--lumo-space-m)) 0;
            border-radius: var(--lumo-border-radius) var(--lumo-border-radius) 0 0;
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
            background-color: rgb(0 177 169);
            padding: var(--lumo-space-xxs) var(--lumo-space-xs);
            border-radius: var(--lumo-border-radius-s);
            box-shadow: 0px 2px 3px rgba(0, 0, 0, 0.16);
            cursor: default;
            animation: 2s ease 2s 3 forwards move;
        }
        @keyframes move {
            0% {
                transform: rotate(0deg);
            }
            12% {
                transform: rotate(-13deg);
            }
            27% {
                transform: rotate(12deg);
            }
            35% {
                transform: rotate(-4deg);
            }
            42% {
                transform: rotate(0deg);
            }
        }
        account-upgrade-view-content .inner-content:nth-child(1) .card-header {
            background-color: var(--pm-grey-color-lightest);
        }
        account-upgrade-view-content .inner-content:nth-child(1) .title {
            color: var(--pm-grey-color-darkest);
        }
        account-upgrade-view-content .inner-content:nth-child(2) .card-header {
            background-color: rgb(230 241 247);
        }
        account-upgrade-view-content .inner-content:nth-child(2) .title {
            color: rgb(107 118 148);
        }
        account-upgrade-view-content .inner-content:nth-child(3) .card-header {
            background: linear-gradient(158deg, rgb(222 245 253), rgb(172 178 255));
            background-size: 160% 160%;
            animation: floating 5s infinite;
        }
        @keyframes floating {
            0% {
                background-position: 0% 0%;
            }
            20% {
                background-position: 15% 15%;
            }
            30% {
                background-position: 18% 27%;
            }
            50% {
                background-position: 27% 16%;
            }
            60% {
                background-position: 8% 12%;
            }
            80% {
                background-position: 3% 5%;
            }
        }
        account-upgrade-view-content .inner-content:nth-child(3) .title {
            color: var(--pm-grey-color-darkest);
        }
        account-upgrade-view-content .inner-content:nth-child(4) .card-header {
            background-color: rgb(233 242 255);
        }
        account-upgrade-view-content .inner-content:nth-child(4) .title {
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
          line-height: 1.1;
          margin: var(--lumo-space-l) auto var(--lumo-space-s);
        }
        account-upgrade-view-content .price {
            font-size: 4rem;
            font-family: var(--lumo-font-family-header);
            font-weight: 500;
            letter-spacing: -.03rem;
            margin-right: var(--lumo-space-xxxs);
        }
        account-upgrade-view-content .price-period {
            font-size: var(--lumo-font-size-s);
            font-family: var(--lumo-font-family-header);
            font-weight: 500;
            letter-spacing: -.02rem;
            margin-right: var(--lumo-space-xxxs);
        }
        account-upgrade-view-content .additional-info {
            font-size: var(--lumo-font-size-s);
            text-align: left;
            margin-top: auto;
        }
        account-upgrade-view-content .features {
            text-align: left;
            padding: 0;
            margin: var(--lumo-space-m) 0 var(--lumo-space-m) var(--lumo-space-m);
        }
        account-upgrade-view-content li {
            list-style: none;
            position: relative;
            font-size: var(--lumo-font-size-s);
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
        account-upgrade-view-content .pro-service-wrapper {
          align-items: center;
          max-width: 800px;
          padding: var(--lumo-space-l);
          margin: calc(var(--lumo-space-xxl) * 1.25) auto;
          border-radius: var(--lumo-border-radius-m);
          box-shadow: 0 8px 26px rgb(0 0 0 / 14%);
        }
        account-upgrade-view-content h2 {
          margin: 0 auto;
        }
        account-upgrade-view-content .pro-service-wrapper p {
          margin: var(--lumo-space-m) auto;
        }
        account-upgrade-view-content .feature-breakdown-wrapper {
          align-items: center;
          background: var(--pm-app-bg-color);
          padding-top: calc(var(--lumo-space-xxl) * 3);
          margin: calc(var(--lumo-space-xxl) * -1.5) auto calc(var(--lumo-space-xxl) * 1.5);
          box-shadow: 0 2rem 1.5rem var(--pm-app-bg-color);
        }
        account-upgrade-view-content .feature-breakdown-table {
          max-width: 1000px;
          width: 80vw;
          margin: var(--lumo-space-l) auto;
        }
        account-upgrade-view-content .feature-breakdown-table vaadin-horizontal-layout {
          align-items: center;
          width: 100%;
          padding: var(--lumo-space-xs);
        }
        account-upgrade-view-content .feature-breakdown-table vaadin-horizontal-layout:not(.header-row):not(.section-row):nth-child(odd) {
          background-color: var(--lumo-primary-color-10pct);
        }
        account-upgrade-view-content .feature-breakdown-table vaadin-horizontal-layout div:first-child {
          flex: 1 0 20%;
          text-align: left;
        }
        account-upgrade-view-content .feature-breakdown-table vaadin-horizontal-layout div:not(:first-child) {
          flex: 0 0 20%;
        }
        account-upgrade-view-content .feature-breakdown-table .header-row {
          border-bottom: 1px solid var(--pm-grey-color-dark);
        }
        account-upgrade-view-content .feature-breakdown-table .header-row h4 {
          margin: 0;
        }
        account-upgrade-view-content .feature-breakdown-table .header-row span {
          font-size: var(--lumo-font-size-s);
        }
        account-upgrade-view-content .feature-breakdown-table h3 {
          margin: 0;
        }
        account-upgrade-view-content .feature-breakdown-table .section-row {
          margin: var(--lumo-space-l) 0 var(--lumo-space-xs);
        }
        account-upgrade-view-content iron-icon {
          --iron-icon-width: var(--lumo-font-size-l);
          --iron-icon-height: var(--lumo-font-size-l);
        }
        account-upgrade-view-content iron-icon[icon="vaadin:close"] {
          color: var(--pm-danger-color);
        }
        account-upgrade-view-content iron-icon[icon="vaadin:check"] {
          color: var(--pm-friendly-color);
        }
        account-upgrade-view-content .feature-breakdown-table a {
          text-decoration: underline;
        }
        account-upgrade-view-content .feature-breakdown-table a:hover {
          text-decoration: none;
        }
        @media screen and (max-width: 767px) {
          account-upgrade-view-content {
            padding: var(--lumo-space-m) 0 var(--lumo-space-l);
          }
          account-upgrade-view-content .feature-breakdown-table {
            max-width: 1000px;
            width: 100%;
            font-size: var(--lumo-font-size-xs);
            margin: var(--lumo-space-l) auto;
          }
          account-upgrade-view-content .feature-breakdown-table vaadin-horizontal-layout {
            padding: var(--lumo-space-xxs);
          }
          account-upgrade-view-content .feature-breakdown-table vaadin-horizontal-layout div:not(:first-child) {
            flex: 0 0 18%;
          }
          account-upgrade-view-content .pro-service-wrapper {
            margin: calc(var(--lumo-space-xxl) * 1.25) var(--lumo-space-m);
          }
        }
      </style>
    <div class="header">
      <h1>Subscription Plans</h1>
      <span>Billed monthly per user</span>
    </div>
    <vaadin-horizontal-layout>
        <vaadin-vertical-layout class="inner-content">
            <vaadin-vertical-layout class="card-header">
                <h2 class="title">Trial</h2>
                <span class="details">For Hobbyists</span>
            </vaadin-vertical-layout>
            <span class="price-cont">
                <span class="price">$0</span>
                <span class="price-period">per month</span>
            </span>
            <ul class="features">
                <li>1 Concurrent Experiment</li>
                <li>Unlimited Policy File Export</li>
            </ul>
            ${this.isTrialUser ? html`<vaadin-button id="freeBtn" theme="tertiary" disabled>Current Plan</vaadin-button>` : null}
        </vaadin-vertical-layout>
        <vaadin-vertical-layout class="inner-content">
            <vaadin-vertical-layout class="card-header">
                <h2 class="title">Academic</h2>
                <span class="details">For Students & Academics</span>
            </vaadin-vertical-layout>
            <span class="price-cont">
                <span class="price">$99</span>
                <span class="price-period">per month</span>
            </span>
            <ul class="features">
                <li>Unlimited Concurrent Experiments</li>
                <li>Unlimited Policy File Export</li>
            </ul>
            ${this.isTrialUser ? html`<a href="${this.contactLink}">
                <vaadin-button id="studentBtn" theme="primary">Contact Us</vaadin-button>
            </a>` : null}
        </vaadin-vertical-layout>
        <vaadin-vertical-layout class="inner-content">
            <vaadin-vertical-layout class="card-header">
                <h2 class="title">Pro</h2>
                <span class="details">For Simulation Engineers</span>
                <span class="popular-tag">POPULAR</span>
            </vaadin-vertical-layout>
            <span class="price-cont">
              <span class="price">$499</span>
              <span class="price-period">per month</span>
            </span>
            <ul class="features">
                <li>Unlimited Concurrent Experiments</li>
                <li>Unlimited Policy File Export</li>
                <li>Premium Technical Support</li>
            </ul>
            <vaadin-button id="proBtn" theme="primary" @click="${this.handleProClick}">Upgrade Now</vaadin-button>
        </vaadin-vertical-layout>
        <vaadin-vertical-layout class="inner-content">
            <vaadin-vertical-layout class="card-header">
                <h2 class="title">Enterprise</h2>
                <span class="details">For Consultancies & Corporate Teams</span>
            </vaadin-vertical-layout>
            <span class="price-cont">
                <span class="price">$999</span>
                <span class="price-period">per month</span>
            </span>
            <ul class="features">
                <li>Unlimited Concurrent Experiments</li>
                <li>Unlimited Policy File Export</li>
                <li>Premium Technical Support</li>
                <li>Full Deployment Options Available</li>
            </ul>
            ${this.isTrialUser ? html`<a href="${this.contactLink}">
                <vaadin-button id="enterpriseBtn" theme="primary">Contact Us</vaadin-button>
            </a>` : null}
        </vaadin-vertical-layout>
    </vaadin-horizontal-layout>
    <vaadin-vertical-layout class="feature-breakdown-wrapper">
      <h2>Feature Breakdown</h2>
      <vaadin-vertical-layout class="feature-breakdown-table">
        <vaadin-horizontal-layout class="header-row">
          <div></div>
          <div><h4>Trial</h4><span>$0/mo</span></div>
          <div><h4>Academic</h4><span>$99/mo</span></div>
          <div><h4>Pro</h4><span>$499/mo</span></div>
          <div><h4>Enterprise</h4><span>$999/mo</span></div>
        </vaadin-horizontal-layout>
        <vaadin-horizontal-layout class="section-row">
          <h3>Policy Training</h3>
        </vaadin-horizontal-layout>
        <vaadin-horizontal-layout>
          <div>Concurrent Experiments</div>
          <div>1</div>
          <div>Unlimited</div>
          <div>Unlimited</div>
          <div>Unlimited</div>
        </vaadin-horizontal-layout>
        <vaadin-horizontal-layout>
          <div>Policy File Export</div>
          <div>Unlimited</div>
          <div>Unlimited</div>
          <div>Unlimited</div>
          <div>Unlimited</div>
        </vaadin-horizontal-layout>
        <vaadin-horizontal-layout>
          <div>Share Experiments Publicly</div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
        </vaadin-horizontal-layout>
        <vaadin-horizontal-layout>
          <div>Bulk Export Policies</div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
        </vaadin-horizontal-layout>
        <vaadin-horizontal-layout>
          <div>Experiment Tracker</div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
        </vaadin-horizontal-layout>
        <vaadin-horizontal-layout>
          <div>Action Masking</div>
          <div><iron-icon icon="vaadin:close"></iron-icon></div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
        </vaadin-horizontal-layout>
        <vaadin-horizontal-layout>
          <div>Policy Freezing</div>
          <div><iron-icon icon="vaadin:close"></iron-icon></div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
        </vaadin-horizontal-layout>
        <vaadin-horizontal-layout>
          <div>Longer Trainings</div>
          <div><iron-icon icon="vaadin:close"></iron-icon></div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
        </vaadin-horizontal-layout>
        <vaadin-horizontal-layout>
          <div>Simulation Parameters</div>
          <div><iron-icon icon="vaadin:close"></iron-icon></div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
        </vaadin-horizontal-layout>
        <vaadin-horizontal-layout>
          <div>Customize Neural Network Layers/Nodes</div>
          <div><iron-icon icon="vaadin:close"></iron-icon></div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
        </vaadin-horizontal-layout>
        <vaadin-horizontal-layout>
          <div>Premium Technical Support</div>
          <div><iron-icon icon="vaadin:close"></iron-icon></div>
          <div><iron-icon icon="vaadin:close"></iron-icon></div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
          <div><iron-icon icon="vaadin:check"></iron-icon></div>
        </vaadin-horizontal-layout>
        <vaadin-horizontal-layout class="section-row">
          <div><h3>Policy Serving</h3>(Add-On)</div>
          <div>+ $0/mo</div>
          <div>+ $99/mo</div>
          <div>
            <a href="${this.contactLink}">Contact Us</a>
          </div>
          <div>
            <a href="${this.contactLink}">Contact Us</a>
          </div>
        </vaadin-horizontal-layout>
        <vaadin-horizontal-layout>
          <div>Additional Seats (Policy Serving Only)</div>
          <div>0</div>
          <div>0</div>
          <div>3</div>
          <div>Unlimited</div>
        </vaadin-horizontal-layout>
        <vaadin-horizontal-layout>
          <div>Offline Exported Policies</div>
          <div>0</div>
          <div>1</div>
          <div>Unlimited</div>
          <div>Unlimited</div>
        </vaadin-horizontal-layout>
        <vaadin-horizontal-layout>
          <div>Online Concurrent Policy Servers</div>
          <div>1</div>
          <div>1</div>
          <div>Unlimited</div>
          <div>Unlimited</div>
        </vaadin-horizontal-layout>
        <vaadin-horizontal-layout>
          <div>Online API Calls</div>
          <div>10/mo</div>
          <div>1,000/mo</div>
          <div>Unlimited</div>
          <div>Unlimited</div>
        </vaadin-horizontal-layout>
        <vaadin-horizontal-layout>
          <div>Integration Support</div>
          <div><iron-icon icon="vaadin:close"></iron-icon></div>
          <div><iron-icon icon="vaadin:close"></iron-icon></div>
          <div>
            <a href="${this.contactLink}">Contact Us</a>
          </div>
          <div>
            <a href="${this.contactLink}">Contact Us</a>
          </div>
        </vaadin-horizontal-layout>
        <vaadin-horizontal-layout>
          <div>SLA</div>
          <div><iron-icon icon="vaadin:close"></iron-icon></div>
          <div><iron-icon icon="vaadin:close"></iron-icon></div>
          <div>
            <a href="${this.contactLink}">Contact Us</a>
          </div>
          <div>
            <a href="${this.contactLink}">Contact Us</a>
          </div>
        </vaadin-horizontal-layout>
      </vaadin-vertical-layout>
    </vaadin-vertical-layout>
    <vaadin-vertical-layout class="pro-service-wrapper">
      <h2>Professional Services</h2>
      <p>Don’t have a simulation model? Pathmind can build a simulation for your real-world use case. Our team is also available for proof of concepts to showcase reinforcement learning to your organization.</p>
      <a href="${this.contactLink}">
        <vaadin-button id="proServicesBtn" theme="primary">Contact Us</vaadin-button>
      </a>
    </vaadin-vertical-layout>
    `;
  }

  createRenderRoot() {
    return this;
  }

  firstUpdated() {
      (this.stripeObj as stripe.Stripe) = window.Stripe(this.key);
  }

  handleProClick() {
    fetch(`${this.apiUrl}/create-checkout-session?type=pro`, {
      method: "POST",
      headers: {
        'X-PM-API-TOKEN': this.userApiKey
      },
    })
    .then(response => {
      if (!response.ok) {
        throw new Error(response.statusText);
      }
      return response.json();
    })
    .then(session => 
      this.stripeObj.redirectToCheckout({ sessionId: session.id })
    )
    .then(result => {
      // If redirectToCheckout fails due to a browser or network
      // error, you should display the localized error message to your
      // customer using error.message.
      if (result.error) {
        alert(result.error.message);
      }
    })
    .catch(error => {
      console.error("Error:", error);
      window.location.pathname = "/page-not-found";
    });
  }
}

customElements.define("account-upgrade-view-content", AccountUpgradeViewContent);
