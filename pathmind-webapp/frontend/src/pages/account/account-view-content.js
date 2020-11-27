import {html, PolymerElement} from "@polymer/polymer/polymer-element.js";
import "../../components/organisms/app-footer.js";

class AccountViewContent extends PolymerElement {
    static get template() {
        return html`
            <style include="shared-styles pathmind-dialog-view">
                :host {
                    justify-content: space-between;
                }
                .panel-wrapper {
                    min-height: auto;
                    overflow: visible;
                }
                .block {
                    align-items: flex-start;
                }
                .small {
                    font-size: var(--lumo-font-size-s);
                }
                .small:empty {
                    display: none;
                }
                vaadin-button {
                    min-width: auto;
                }
            </style>
            <vaadin-horizontal-layout class="panel-wrapper">
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
                        <vaadin-button id="editInfoBtn" theme="small">
                            Edit
                        </vaadin-button>
                    </vaadin-horizontal-layout>
                    <vaadin-horizontal-layout style="width: 100%;" class="block border-top">
                        <vaadin-vertical-layout class="info">
                            <div class="title">Password</div>
                            <div class="data">* * * * * * * * *</div>
                        </vaadin-vertical-layout>
                        <vaadin-button id="changePasswordBtn" theme="small">
                            Change
                        </vaadin-button>
                    </vaadin-horizontal-layout>
                    <vaadin-horizontal-layout style="width: 100%;" class="block border-top">
                        <vaadin-vertical-layout class="info" style="width: 100%;">
                            <div class="title">API Key</div>
                            <div class="data">{{apiKey}}</div>
                            <vaadin-horizontal-layout style="width: 100%;" class="block">
                                <div class="data small">{{apiKeyExpiresPhrase}}</div>
                                <vaadin-button id="rotateApiKeyBtn" theme="small">
                                    Rotate
                                </vaadin-button>
                            </vaadin-horizontal-layout>
                        </vaadin-vertical-layout>
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
                        <vaadin-button id="editPaymentBtn" theme="small">
                            Edit
                        </vaadin-button>
                    </vaadin-horizontal-layout>
                </vaadin-vertical-layout>
            </div>
        </vaadin-horizontal-layout>
        <app-footer 
            privacylink="{{privacyLink}}"
            termslink="{{termsOfUseLink}}"
            contactlink="{{contactLink}}"
        ></app-footer>`;
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
