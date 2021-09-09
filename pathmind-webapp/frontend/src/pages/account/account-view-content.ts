import { LitElement, html, property } from "lit-element";
import "../../components/organisms/app-footer.ts";

class AccountViewContent extends LitElement {

    @property({type: String})
    email = "";
    @property({type: String})
    firstName = "";
    @property({type: String})
    lastName = "";
    @property({type: String})
    apiKey = "";
    @property({type: String})
    apiKeyExpiresPhrase = "";
    @property({type: String})
    subscription = "";
    @property({type: String})
    subscriptionCancellationNote = "";

    render() {
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
                account-view-content vaadin-button[theme~="small"] {
                    height: 1.8rem;
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
                #rotateApiKeyBtn {
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
                .subscription-wrapper {
                    justify-content: space-between;
                    width: 100%;
                }
                .subscription-hint {
                    font-size: var(--lumo-font-size-xs);
                    color: var(--lumo-secondary-text-color);
                }
            </style>
            <vaadin-horizontal-layout class="panel-wrapper">
              <div class="content">
                <span class="section-title-label">Account</span>
                <vaadin-vertical-layout style="width: 100%;" class="inner-content">
                    <vaadin-horizontal-layout style="width: 100%;" class="block">
                        <vaadin-vertical-layout class="info">
                            <div class="title">User Email</div>
                            <div class="data">${this.email}</div>
                            <div class="title">First Name</div>
                            <div class="data">${this.firstName}</div>
                            <div class="title">Last Name</div>
                            <div class="data">${this.lastName}</div>
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
                                <div id="apiExpiryDate">${this.apiKeyExpiresPhrase}</div>
                                <vaadin-button id="rotateApiKeyBtn" theme="tertiary action-button small">
                                    <iron-icon icon="vaadin:refresh"></iron-icon>
                                </vaadin-button>
                            </vaadin-horizontal-layout>
                            <vaadin-vertical-layout class="data" style="width: 100%">
                                <div id="accessToken">${this.apiKey}</div>
                                <vaadin-button id="apiCopyBtn" theme="small" @click="${this.copyApi}">
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
                        <vaadin-vertical-layout style="width: 100%;">
                            <vaadin-horizontal-layout class="subscription-wrapper">
                                <vaadin-vertical-layout class="info">
                                    <div class="title">Current Subscription</div>
                                    <div class="data">${this.subscription}</div>
                                </vaadin-vertical-layout>
                                <vaadin-button id="upgradeBtn" theme="small">
                                    Upgrade
                                </vaadin-button>
                                <vaadin-button id="cancelSubscriptionBtn" theme="error small" @click="${event => (this as any).$server.cancelSubscription()}">
                                    Cancel
                                </vaadin-button>
                            </vaadin-horizontal-layout>
                            <div class="data subscription-hint">${this.subscriptionCancellationNote}</div>
                        </vaadin-vertical-layout>
                    </vaadin-horizontal-layout>
                </vaadin-vertical-layout>
            </div>
        </vaadin-horizontal-layout>
        <app-footer></app-footer>`;
    }

    createRenderRoot() {
      return this;
    }

    firstUpdated() {

        document.getElementById("accessToken").addEventListener("copy", event => {
            // This will handle the clipboard data to eliminate extra linebreak at the end of the string
            const selection = document.getElementById("accessToken").innerHTML.replace(/\s/g, '').replace(/\W{2,}/g, '');
            if (event.clipboardData) {
                event.clipboardData.setData("text/plain", selection);
            } else {
                (window as any).clipboardData.setData("text", selection);
            }
            event.preventDefault();
        });

        (this as any).$server.setSubscriptionEndDate();
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
        checkmarkIcon.setAttribute("active", "true");
        setTimeout(function() {
            copyIcon.setAttribute("active", "true");
            checkmarkIcon.removeAttribute("active");
        }, 1200);
    }
}

customElements.define("account-view-content", AccountViewContent);
