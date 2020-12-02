import {html, PolymerElement} from "@polymer/polymer/polymer-element.js";

class AccountViewContent extends PolymerElement {
    static get template() {
        return html`
            <style>
                account-view-content .block {
                    align-items: flex-start;
                }
                account-view-content .small {
                    font-size: var(--lumo-font-size-s);
                }
                account-view-content .small:empty {
                    display: none;
                }
                .info {
                    position: relative;
                }
                #rotateApiMenu {
                    margin-right: calc(-1 * var(--lumo-space-xs));
                }
                #rotateApiKeyBtn {
                    display: none;
                }
                .api-title-wrapper {
                    justify-content: flex-end;
                    align-items: center;
                    width: 100%;
                }
                .api-title-wrapper .title {
                    justify-self: flex-start;
                    flex: 1;
                    text-align: left;
                }
                #apiExpiryDate {
                    color: var(--lumo-secondary-text-color);
                    font-size: var(--lumo-font-size-xs);
                }
                #small-menu {
                    width: auto;
                    margin: 0;
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
                            <vaadin-horizontal-layout class="api-title-wrapper">
                                <div class="title">Access Token</div>
                                <div id="apiExpiryDate">{{apiKeyExpiresPhrase}}</div>
                                <vaadin-context-menu id="rotateApiMenu">
                                    <template>
                                        <vaadin-list-box>
                                            <vaadin-item on-click="triggerRotateBtn">
                                                Rotate
                                            </vaadin-item>
                                        </vaadin-list-box>
                                    </template>
                                    <vaadin-button id="rotateApiKeyBtn" theme="small"></vaadin-button>
                                    <vaadin-button id="small-menu" theme="tertiary small">
                                        <iron-icon icon="vaadin:ellipsis-dots-h"></iron-icon>
                                    </vaadin-button>
                                </vaadin-context-menu>
                            </vaadin-horizontal-layout>
                            <div class="data">{{apiKey}}</div>
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
                <div class="support-cont">
                    <a href="{{privacyLink}}" target="_blank">Privacy Policy</a>
                    <a href="{{termsOfUseLink}}" target="_blank">Terms of Use</a><br>
                    <a class="support" href="{{contactLink}}">Contact Support</a>
                </div>
            </div>
        </vaadin-horizontal-layout>`;
    }

    _attachDom(dom) {
        this.appendChild(dom);
    }

    ready() {
        super.ready();
        document.getElementById("rotateApiMenu")._setProperty("openOn", "click");
    }

    triggerRotateBtn() {
        document.getElementById("rotateApiKeyBtn").click();
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
