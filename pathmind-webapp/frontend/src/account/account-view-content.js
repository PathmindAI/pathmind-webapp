import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class AccountViewContent extends PolymerElement {
    static get template() {
        return html`
            <style include="shared-styles pathmind-dialog-view">
                .small {
                    font-size: small;
                }
                .small:empty {
                    display: none;
                }
                vaadin-button {
                    min-width: auto;
                }
            </style>
            <div class="content">
                <span class="section-title-label">Account</span>
                <vaadin-vertical-layout style="width: 100%;" class="inner-content">
                    <vaadin-horizontal-layout style="width: 100%;" class="block">
                        <vaadin-vertical-layout class="info">
                            <div class="title">User Email</div>
                            <div class="data">{{email}}</div>
                            <div class="title">First Name</div>
                            <div class="data">{{firstName}}</div>
                            <div class="title">Last Name</div>
                            <div class="data">{{lastName}}</div>
                        </vaadin-vertical-layout>
                        <vaadin-button id="editInfoBtn">
                            Edit
                        </vaadin-button>
                    </vaadin-horizontal-layout>
                    <vaadin-horizontal-layout style="width: 100%;" class="block border-top">
                        <vaadin-vertical-layout class="info">
                            <div class="title">Password</div>
                            <div class="data">* * * * * * * * *</div>
                        </vaadin-vertical-layout>
                        <vaadin-button id="changePasswordBtn">
                            Change
                        </vaadin-button>
                    </vaadin-horizontal-layout>
                    <vaadin-horizontal-layout style="width: 100%;" class="block border-top">
                        <vaadin-vertical-layout class="info">
                            <div class="title">Current Subscription</div>
                            <div class="data">{{subscription}}</div>
                            <div class="data small">{{subscriptionCancellationNote}}</div>
                        </vaadin-vertical-layout>
                        <vaadin-button id="upgradeBtn">
                            Upgrade
                        </vaadin-button>
                        <vaadin-button id="cancelSubscriptionBtn" theme="error">
                            Cancel
                        </vaadin-button>
                    </vaadin-horizontal-layout>
                    <vaadin-horizontal-layout style="width: 100%;" class="block border-top">
                        <vaadin-vertical-layout class="info">
                            <div class="title">Payment</div>
                            <div class="data">{{billingInfo}}</div>
                        </vaadin-vertical-layout>
                        <vaadin-button id="editPaymentBtn">
                            Edit
                        </vaadin-button>
                    </vaadin-horizontal-layout>
                </vaadin-vertical-layout>
                <div class="support-cont">
                    <a href="{{privacyLink}}" target="_blank">Privacy Policy</a>
                    <a href="{{termsOfUseLink}}" target="_blank">Terms of Use</a>
                </div>
                <a class="support" href="{{contactLink}}">Contact Support</a>
            </div>
        `;
    }

    static get is() {
        return "account-view-content";
    }

    static get properties() {
        return {
            // Declare your properties here.
        };
    }
}

customElements.define(AccountViewContent.is, AccountViewContent);
