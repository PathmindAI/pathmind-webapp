import {html, PolymerElement} from "@polymer/polymer/polymer-element.js";
import "../../components/organisms/app-footer.js";

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
                vaadin-button[theme~="small"] {
                    height: 1.8rem;
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
                #apiCopyBtn {
                    position: relative;
                    align-self: flex-end;
                }
                #apiCopyBtn span {
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    position: absolute;
                    width: 100%;
                    top: 50%;
                    left: -.25rem;
                    opacity: 0;
                    transition: opacity 0.3s;
                    transform: translateY(-50%);
                }
                #apiCopyBtn span iron-icon {
                    width: 1rem;
                    height: 1rem;
                    margin-right: var(--lumo-space-xxs);
                }
                #apiCopyBtn span[active] {
                    opacity: 1;
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
                                        <vaadin-context-menu-list-box>
                                            <vaadin-item on-click="triggerRotateBtn">
                                                Rotate
                                            </vaadin-item>
                                        </vaadin-context-menu-list-box>
                                    </template>
                                    <vaadin-button id="rotateApiKeyBtn" theme="small"></vaadin-button>
                                    <vaadin-button id="small-menu" theme="tertiary small">
                                        <iron-icon icon="vaadin:ellipsis-dots-h"></iron-icon>
                                    </vaadin-button>
                                </vaadin-context-menu>
                            </vaadin-horizontal-layout>
                            <vaadin-vertical-layout class="data" style="width: 100%">
                                <div id="accessToken">{{apiKey}}</div>
                                <vaadin-button id="apiCopyBtn" theme="small" on-click="copyApi">
                                    <span active>
                                        <iron-icon icon="vaadin:copy-o"></iron-icon>Copy
                                    </span>
                                    <span>
                                        <iron-icon icon="vaadin:check"></iron-icon>Copied
                                    </span>
                                </vaadin-button>
                            </vaadin-vertical-layout>
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

    _attachDom(dom) {
        this.appendChild(dom);
    }

    ready() {
        super.ready();
        document.getElementById("rotateApiMenu")._setProperty("openOn", "click");

        document.getElementById("accessToken").addEventListener("copy", event => {
            // This will handle the clipboard data to eliminate extra linebreak at the end of the string
            const selection = document.getElementById("accessToken").innerHTML.replace(/\s/g, '');
            if (event.clipboardData) {
                event.clipboardData.setData("text/plain", selection);
            } else {
                window.clipboardData.setData("text", selection);
            }
            event.preventDefault();
        });
    }

    triggerRotateBtn() {
        document.getElementById("rotateApiKeyBtn").click();
    }

    copyApi(event) {
        const copyButton = document.getElementById("apiCopyBtn");
        const copyIcon = copyButton.querySelector("span:first-child");
        const checkmarkIcon = copyButton.querySelector("span:last-child");
        const range = document.createRange();
        range.selectNode(document.getElementById("accessToken"));
        const select = window.getSelection();
        select.removeAllRanges();
        select.addRange(range);
        document.execCommand("copy");
        select.removeAllRanges();

        copyIcon.removeAttribute("active");
        checkmarkIcon.setAttribute("active", true);
        setTimeout(function() {
            copyIcon.setAttribute("active", true);
            checkmarkIcon.removeAttribute("active");
        }, 1200);
    }

    static get is() {
        return "account-view-content";
    }
}

customElements.define(AccountViewContent.is, AccountViewContent);
