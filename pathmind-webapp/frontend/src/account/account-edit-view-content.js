import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class AccountEditViewContent extends PolymerElement {
    static get template() {
        return html`
            <style include="shared-styles pathmind-dialog-view"></style>
            <div class="content">
                <span class="section-title-label">Edit Account</span>
                <vaadin-vertical-layout style="width: 100%;" class="inner-content">
                    <vaadin-vertical-layout style="width: 100%; height: 100%;">
                        <vaadin-text-field id="email" label="Email"></vaadin-text-field>
                        <vaadin-text-field id="firstName" label="First Name"></vaadin-text-field>
                        <vaadin-text-field id="lastName" label="Last Name"></vaadin-text-field>
                        <vaadin-vertical-layout id="buttonsCont">
                            <vaadin-button id="updateBtn" theme="primary">
                                Update
                            </vaadin-button>
                            <vaadin-button id="cancelBtn" theme="tertiary">Cancel</vaadin-button>
                        </vaadin-vertical-layout>
                    </vaadin-vertical-layout>
                </vaadin-vertical-layout>
                <a class="support" href="{{contactLink}}">Contact Support</a>
            </div>
        `;
    }

    static get is() {
        return "account-edit-view-content";
    }

    static get properties() {
        return {
            // Declare your properties here.
        };
    }
}

customElements.define(AccountEditViewContent.is, AccountEditViewContent);
